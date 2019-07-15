package com.example.hometask

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField

class Client {
    @DatabaseField(generatedId = true)
    var id: Int = 0
    @DatabaseField(canBeNull = false)
    var surname: String = ""
    @DatabaseField(canBeNull = false)
    var name: String = ""
    @DatabaseField(canBeNull = false)
    var patronymic: String = ""
    @DatabaseField(canBeNull = false)
    var position: String = ""
    @DatabaseField(canBeNull = false)
    var date: String = ""
    @ForeignCollectionField(eager = true)
    var childForeignCollectionField: Collection<Relation>? = null

    constructor() {}

    constructor(surname: String, name: String, patronymic: String, position: String, date: String) {
        this.surname = surname
        this.name = name
        this.patronymic = patronymic
        this.position = position
        this.date = date
    }

    override fun toString(): String {
        return "Client{" +
                "id=" + id +
                ", surname='" + surname + '\''.toString() +
                ", name='" + name + '\''.toString() +
                ", patronymic='" + patronymic + '\''.toString() +
                ", position='" + position + '\''.toString() +
                ", date='" + date + '\''.toString() +
                '}'.toString()
    }
}
