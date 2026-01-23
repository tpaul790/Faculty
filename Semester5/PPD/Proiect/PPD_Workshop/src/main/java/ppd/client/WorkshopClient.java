package ppd.client;

import ppd.config.WorkshopConfig;
import ppd.dto.Request;
import ppd.dto.RequestType;
import ppd.dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class WorkshopClient implements Runnable {
    private final String name;
    private final String cnp;

    public WorkshopClient(String name, String cnp) {
        this.name = name;
        this.cnp = cnp;
    }

    @Override
    public void run() {
        try {
            int center = ThreadLocalRandom.current().nextInt(1, WorkshopConfig.NUM_CENTRES + 1);
            int workshop = ThreadLocalRandom.current().nextInt(1, WorkshopConfig.NUM_WORKSHOPS + 1);
            int hour = ThreadLocalRandom.current().nextInt(8, 18); // make sure that sometimes the request is out of the working hours

            // Send Reservation Request
            CompletableFuture<Response> resFuture = sendRequestAsync(
                    new Request(name, cnp, center, workshop, hour)
            );
            Response resResp = resFuture.get(); // Wait for result

            if (resResp.isSuccess()) {
                System.out.println(name + ": Reservation SUCCESS ID: " + resResp.getReservationId());

                //Random wait, allow some reservations to expire
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 15000));

                // Send Payment Request
                CompletableFuture<Response> payFuture = sendRequestAsync(
                        new Request(RequestType.PAY, resResp.getReservationId(), resResp.getCost())
                );
                Response payResp = payFuture.get();

                if (payResp.isSuccess()) {
                    System.out.println(name + ": Payment Confirmed.");

                    // Optional Cancellation (Random chance)
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        Thread.sleep(1000);
                        CompletableFuture<Response> cancelFuture = sendRequestAsync(
                                new Request(RequestType.CANCEL, resResp.getReservationId(), 0)
                        );
                        Response cancelResp = cancelFuture.get();
                        System.out.println(name + ": Cancel -> " + cancelResp.getMessage());
                    }
                } else {
                    System.out.println(name + ": Payment FAILED (" + payResp.getMessage() + ")");
                }

            } else {
                System.out.println(name + ": Reservation FAILED (" + resResp.getMessage() + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CompletableFuture<Response> sendRequestAsync(Request req) {
        return CompletableFuture.supplyAsync(() -> {
            try (Socket socket = new Socket("localhost", 9000);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject(req);
                return (Response) in.readObject();
            } catch (Exception e) {
                return new Response(false, "Comm Error: " + e.getMessage(), null, 0);
            }
        });
    }
}