package com.example.hometask

import com.j256.ormlite.field.DatabaseField

class Child {
    @DatabaseField(generatedId = true)
    var id: Int = 0
    @DatabaseField(foreign = true, foreignAutoCreate = true)
    var client: Client? = null
    @DatabaseField(canBeNull = false)
    var nameChild: String = ""


    constructor() {}

    constructor(nameChild: String) : super() {
        this.nameChild = nameChild
    }

    constructor(client: Client, nameChild: String) : super() {
        this.client = client
        this.nameChild = nameChild
    }

    override fun toString(): String {
        return "Child{" +
                "id=" + id +
                ", client=" + client +
                ", nameChild='" + nameChild + '\''.toString() +
                '}'.toString()
    }
}
