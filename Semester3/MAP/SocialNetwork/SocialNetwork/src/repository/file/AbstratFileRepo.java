package repository.file;

import domain.Entity;
import domain.validation.Validator;
import repository.memory.MemoryRepo;

import java.io.*;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstratFileRepo<ID,E extends Entity<ID>> extends MemoryRepo<ID,E> {
    private final String fileName;

    public abstract E entityFromString(String line);
    public abstract String entityToString(E entity);
    public abstract String makeHeader();

    public AbstratFileRepo(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadFromFile();
    }

    private void loadFromFile(){
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            br.readLine(); //for header
            String line;
            while((line = br.readLine()) != null){
                E entity = entityFromString(line);
                super.save(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){
            bw.write(makeHeader());
            bw.newLine();
            Consumer<E> consumer = entity -> {
                String line = entityToString(entity);
                try {
                    bw.write(line);
                    bw.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
            findAll().forEach(consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if(e.isEmpty())
            saveToFile();
        return e;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> e = super.update(entity);
        if(e.isEmpty())
            saveToFile();
        return e;
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> e = super.delete(id);
        if(e.isPresent())
            saveToFile();
        return e;
    }
}
