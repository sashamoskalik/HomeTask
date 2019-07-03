package com.example.hometask;

import com.j256.ormlite.field.DatabaseField;

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
