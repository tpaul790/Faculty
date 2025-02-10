package repo;

import exception.RepoException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class GenericRepoInFile<T>{
    private Set<T> objects;
    private String fileName;

    public GenericRepoInFile(String fileName) {
        objects = new HashSet<>();
        this.fileName = fileName;
        loadFromFile();
    }

    public void add(T obj) throws RepoException {
        int curentSize = objects.size();
        objects.add(obj);
        if(curentSize == objects.size()){
            throw new RepoException("This object has already been added!");
        }
        saveToFile();
    }

    public void remove(T obj) throws RepoException {
        int curentSize = objects.size();
        objects.remove(obj);
        if(curentSize == objects.size()){
            throw new RepoException("This object dosen't exist!");
        }
        saveToFile();
    }

    public void update(T obj1, T obj2) throws RepoException {
        int curentSize = objects.size();
        objects.remove(obj1);
        if(curentSize == objects.size()){
            throw new RepoException("This object dosen't exist!");
        }
        objects.add(obj2);
        saveToFile();
    }

    public Set<T> getAll() {
        return objects;
    }

    public String getFileName(){ return fileName; }

    public abstract void loadFromFile();

    public abstract void saveToFile();
}
