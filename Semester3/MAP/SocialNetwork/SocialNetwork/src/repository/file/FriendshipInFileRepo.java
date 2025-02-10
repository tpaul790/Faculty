package repository.file;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class FriendshipInFileRepo extends AbstratFileRepo<Tuple<Integer,Integer>, Friendship> {
    public FriendshipInFileRepo(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Friendship entityFromString(String line) {
        Function<String[], Friendship> stringListToFriendship = fields -> {
            int id1 = Integer.parseInt(fields[0]);
            int id2 = Integer.parseInt(fields[1]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate createTime = LocalDate.parse(fields[2], formatter);
            LocalDate acceptTime = null;
            if (!fields[3].equals("null"))
                acceptTime = LocalDate.parse(fields[3], formatter);
            String status = fields[4];
            return new Friendship(new Tuple<>(id1,id2),createTime,acceptTime, status);
        };
        String[] fields = line.split(",");
        return stringListToFriendship.apply(fields);
    }

    @Override
    public String entityToString(Friendship friendship) {
        return friendship.getId().getFirst() + "," + friendship.getId().getSecond()+","+friendship.getCreateDate()+","+friendship.getAcceptDate()+','+friendship.getStatus();
    }

    @Override
    public String makeHeader() {
        return "UserId1,UserId2,CreateDate,AcceptDate,Status";
    }
}
