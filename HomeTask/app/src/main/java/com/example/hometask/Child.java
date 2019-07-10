package com.example.hometask;

import com.j256.ormlite.field.DatabaseField;

public class Child {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField(foreign = true, foreignAutoCreate = true)
    Client client;
    @DatabaseField(canBeNull = false)
    String nameChild;


    public Child(){
    }

    public Child(String nameChild){
        super();
        this.nameChild = nameChild;
    }

    public Child(Client client, String nameChild) {
        super();
        this.client = client;
        this.nameChild = nameChild;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameChild() {
        return nameChild;
    }

    public void setNameChild(String nameChild) {
        this.nameChild = nameChild;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", client=" + client +
                ", nameChild='" + nameChild + '\'' +
                '}';
    }
}
