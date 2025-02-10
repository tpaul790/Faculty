package repo;

import domain.User;
import exception.RepoException;

import java.io.*;

public class Network extends GenericRepoInFile<User>{

    public Network(String fileName) {
        super(fileName);
    }

    public User findById(int id) throws RepoException {
        for(User user : super.getAll()){
            if(user.getId() == id){
                return user;
            }
        }
        throw new RepoException("There is no user with this id!");
    }

    @Override
    public void loadFromFile() {
        try{
            BufferedReader br = new BufferedReader(new FileReader(super.getFileName()));
            br.readLine();//header ul
            String line;
            while((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                int id = Integer.parseInt(tokens[0]);
                String username = tokens[1];
                String email = tokens[2];
                String password = tokens[3];
                String role = tokens[4];
                super.getAll().add(new User(id, username, email, password, role));
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveToFile() {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(super.getFileName()));
            bw.write("Id,Username,Email,Password,Role");
            bw.newLine();
            for(User user : super.getAll()) {
                bw.write(user.getId() + ","+user.getUsername() + ","+user.getEmail() + ","+user.getPassword() + ","+user.getRole());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
