package com.example.hometask;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Collection;

public class Client {
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField(canBeNull = false)
    String surname;
    @DatabaseField(canBeNull = false)
    String name;
    @DatabaseField(canBeNull = false)
    String patronymic;
    @DatabaseField(canBeNull = false)
    String position;
    @DatabaseField(canBeNull = false)
    String date;
    @ForeignCollectionField(eager = true)
    ForeignCollection<Child> childForeignCollectionField;

    public Client(){
    }

    public Client(String surname, String name, String patronymic, String position, String date) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.position = position;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ForeignCollection<Child> getChildForeignCollectionField() {
        return childForeignCollectionField;
    }

    public void setChildForeignCollectionField(ForeignCollection<Child> childForeignCollectionField) {
        this.childForeignCollectionField = childForeignCollectionField;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", position='" + position + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
