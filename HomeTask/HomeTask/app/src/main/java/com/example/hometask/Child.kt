package com.example.hometask

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField

class Child {
    @DatabaseField(generatedId = true)
    var id: Int = 0
    @DatabaseField(canBeNull = false)
    var nameChild: String = ""
    @ForeignCollectionField(eager = true)
    var foreignCollectionField: Collection<Relation>? = null

    constructor()

    constructor(nameChild: String) : super() {
        this.nameChild = nameChild
    }

    override fun toString(): String {
        return "Child(id=$id, nameChild='$nameChild', foreignCollectionField=$foreignCollectionField)"
    }


}
