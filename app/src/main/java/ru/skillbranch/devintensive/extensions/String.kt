package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16): String{
    var countSymb = 1
    if(count >= 1) countSymb  = count
    if(this.toString().length < countSymb){
        return this.toString().trimEnd()
    }
    else{
        return this.substring(0,countSymb).trimEnd() + "..."
    }
}

fun String.stripHtml(): String{
    return this.replace(Regex("<[^<]*?>|&(.)+;"),"").
        replace(Regex("[^\\S\\r\\n]+")," ")
}