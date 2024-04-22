package nathan.timothy.prog39402finalproject

import androidx.room.TypeConverter

class DatabaseTypeConverter {
    @TypeConverter
    fun fromListStringToString(stringList: List<String>): String = stringList.toString()
    @TypeConverter
    fun toStringListFromString(stringList: String): List<String> {
        val result = ArrayList<String>()
        //val split =stringList.replace("[","").replace("]","").replace(" ","").split(",")
        val split =stringList.replace("[","").replace("]","").split(",")
        for (n in split) {
            try {
                result.add(n.toString())
            } catch (e: Exception) {

            }
        }
        return result
    }

}