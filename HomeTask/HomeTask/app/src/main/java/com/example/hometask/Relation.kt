package com.example.hometask

import com.j256.ormlite.field.DatabaseField

class Relation{
    @DatabaseField(generatedId = true)
    var id: Int = 0
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    var client: Client? = null
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    var child: Child? = null

    constructor()

    constructor(client: Client, child: Child){
        this.client = client
        this.child = child
    }

}