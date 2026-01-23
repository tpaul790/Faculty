package ppd.client;

import ppd.config.WorkshopConfig;
import ppd.data.Database;
import ppd.dto.Reservation;
import ppd.dto.Transaction;
import ppd.dto.TransactionType;
import ppd.dto.Request;
import ppd.dto.Response;
import ppd.dto.ReservationStatus;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private static final int PAYMENT_TIMEOUT_SEC = 10;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Request req = (Request) in.readObject();
            Response resp = processRequest(req);
            out.writeObject(resp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response processRequest(Request req) {
        return switch (req.getType()) {
            case RESERVE -> handleReserve(req);
            case PAY -> handlePayment(req);
            case CANCEL -> handleCancel(req);
        };
    }

    private Response handleReserve(Request req) {
        int startHour = req.getHour();
        int workshopId = req.getWorkshopId();

        int durationMinutes = WorkshopConfig.getDurationMinutes(workshopId);
        int durationHours = durationMinutes / 60;

        if (startHour < 9 || (startHour + durationHours) > 17) {
            return new Response(false, "Out of program - Center opens at 09:00 and close at 17:00", null, 0);
        }

        ReentrantLock lock = Database.getLock(req.getCenterId(), workshopId);
        lock.lock();
        try {
            int cap = WorkshopConfig.getCapacity(req.getCenterId(), workshopId);

            for (int h = 0; h < durationHours; h++) {
                int checkHour = startHour + h;

                long activeCount = Database.countActiveReservations(req.getCenterId(), workshopId, checkHour);

                if (activeCount >= cap) {
                    return new Response(false, "Overpass capacity at:  " + checkHour, null, 0);
                }
            }

            double cost = WorkshopConfig.getCost(workshopId);
            Reservation res = new Reservation(req.getClientName(), req.getCnp(), req.getCenterId(), workshopId, startHour, cost);

            Database.insertReservation(res);

            return new Response(true, "Reservation Successfully", res.getId(), cost);

        } finally {
            lock.unlock();
        }
    }

    private Response handlePayment(Request req) {
        //For a reservation: id, centerId and workshopId are always the same
        Reservation tempRes = Database.getReservationById(req.getReservationId());
        if (tempRes == null) return new Response(false, "Reservation not found!", null, 0);

        ReentrantLock lock = Database.getLock(tempRes.getCenterId(), tempRes.getWorkshopId());
        lock.lock();
        try {
            //Read clean data
            Reservation res = Database.getReservationById(req.getReservationId());

            if ( res == null) {
                return new Response(false, "Reservation not found!", null, 0);
            }

            //PeriodicChecker probably updated the status already
            if( res.getStatus() == ReservationStatus.EXPIRED){
                return new Response(false, "Reservation already expired!", null, 0);
            }

            if (Duration.between(res.getCreationTimestamp(), LocalDateTime.now()).getSeconds() > PAYMENT_TIMEOUT_SEC &&
                res.getStatus() != ReservationStatus.EXPIRED) {
                Database.updateReservationStatus(res.getId(), ReservationStatus.EXPIRED);
                return new Response(false, "Too much time passed from the reservation initialization!", null, 0);
            }

            Database.updateReservationStatus(res.getId(), ReservationStatus.PAID);
            Transaction tx = new Transaction(res.getId(), TransactionType.PAYMENT, req.getAmount());
            Database.insertTransaction(tx);

            return new Response(true, "Payment confirmed!", res.getId(), 0);

        } finally {
            lock.unlock();
        }
    }

    private Response handleCancel(Request req) {
        Reservation tempRes = Database.getReservationById(req.getReservationId());
        if (tempRes == null) return new Response(false, "Reservation not found!", null, 0);

        ReentrantLock lock = Database.getLock(tempRes.getCenterId(), tempRes.getWorkshopId());
        lock.lock();
        try {
            Reservation res = Database.getReservationById(req.getReservationId());

            if (res != null && res.getStatus() == ReservationStatus.PAID) {
                Database.updateReservationStatus(res.getId(), ReservationStatus.CANCELLED);
                Transaction tx = new Transaction(res.getId(), TransactionType.REFUND, -res.getCost());
                Database.insertTransaction(tx);
                return new Response(true, "Reservation successfully cancelled!", res.getId(), 0);
            }
            return new Response(false, "Cannot cancel!", null, 0);
        } finally {
            lock.unlock();
        }
    }
}