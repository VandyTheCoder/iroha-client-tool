package me.lucifer.model

data class IrohaCommand(
    val action: String,
    val objectValue: String,
    val txHashBytes: ByteArray,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IrohaCommand

        if (action != other.action) return false
        if (objectValue != other.objectValue) return false
        if (!txHashBytes.contentEquals(other.txHashBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = action.hashCode()
        result = 31 * result + objectValue.hashCode()
        result = 31 * result + txHashBytes.contentHashCode()
        return result
    }
}