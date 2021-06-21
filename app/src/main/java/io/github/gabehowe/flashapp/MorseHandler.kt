package io.github.gabehowe.flashapp

class MorseHandler {
    companion object {
        val testString = "Lorem ipsum dolor set"
        val unit = 100
        fun translateMorse(string: String) : String {
            val morse = Morse().morse
            var finalString = ""
            for (i in (string.toCharArray())) {
                val char = i.toLowerCase()
                if (morse[char] == null) {
                    continue
                }
                finalString += "${morse[char]} ${if (char != ' ') "/" else ""}"
            }
            finalString = finalString.replace(" ", "")
            return finalString
        }


        fun translateFlashes(morseString: String) : MutableList<Int> {
            val unitArray = mutableListOf<Int>()
            for (i in morseString.toCharArray()) {
                if (i != '.' && i != '-' && i != '/' && i != ';' && i != ',') {
                    throw Exception("Invalid morse character '$i'")
                }
                if (i == '.') {
                    unitArray.add(1)
                }
                if (i == ',') {
                    unitArray.add(0)
                }
                else if (i == '-') {
                    unitArray.add(1)
                    unitArray.add(1)
                    unitArray.add(1)
                }
                else if (i == '/') {
                    unitArray.add(0)
                    unitArray.add(0)
                    unitArray.add(0)
                }
                else if (i == ';') {
                    unitArray.add(0)
                    unitArray.add(0)
                    unitArray.add(0)
                    unitArray.add(0)
                    unitArray.add(0)
                    unitArray.add(0)
                    unitArray.add(0)
                }
            }
            return unitArray
        }
    }
}