package chess


var firstPlayer = "player1"
var secondPlayer = "player2"
var gameOn = true
val validMoveName = Regex("[a-h][1-8][a-h][1-8]")
var enPassantIndices1= false
var enPassantIndices2= false
var numWhitePieces = 0
var numBlackPieces = 0
var stalemate = false
val  piecesPositions = mutableListOf(
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "}
)
var previousPiecesPositions =  mutableListOf(
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "},
    MutableList(8) {" "}
)
lateinit var chessSquares: MutableList<MutableList<String>>
val files = mutableListOf("8", "7", "6", "5", "4", "3", "2", "1")
val ranks = mutableListOf("a", "b", "c", "d", "e", "f", "g", "h")
const val firstPlayerStartPosition = 6
const val secondPlayerStartPosition = 1

fun main() {
//    write your code here

    chessSquares = mutableListOf(
        MutableList(8) {" "},
        MutableList(8) {" "},
        MutableList(8) {" "},
        MutableList(8) {" "},
        MutableList(8) {" "},
        MutableList(8) {" "},
        MutableList(8) {" "},
        MutableList(8) {" "}
    )

    for (y in  0..7) {
        for (x in 0..7) {
            chessSquares[y][x] = ranks[y] + files[x]
        }
    }


    println("Pawns-Only Chess")
    println("First Player's name:")
    firstPlayer = readLine()!!
    println("Second Player's name:")
    secondPlayer = readLine()!!

    var gameTurn = 0

    var playerMove = " "
    var currentPlayer = firstPlayer

    startGame(piecesPositions)

    while (gameOn) {

        currentPlayer = if(gameTurn % 2 == 0) firstPlayer else secondPlayer

        chessBoard(piecesPositions)


        if (numWhitePieces == 0 && gameTurn != 0) {
            println("Black Wins!")
            println("Bye!")
            return
        }
        if (numBlackPieces == 0 && gameTurn != 0) {
            println("White Wins!")
            println("Bye!")
            return
        }

        if (!pieceIsNotStucked(if (currentPlayer == firstPlayer) "W" else "B")) {
            println("Stalemate!")
            println("Bye!")
            return
        }
        
        for (square in piecesPositions[0]) {
            if (square == "W") {
                println("White Wins!")
                println("Bye!")
                return
            }
        }

        for (square in piecesPositions[7]) {
            if (square == "B") {
                println("Black Wins!")
                println("Bye!")
                return
            }
        }



        /*for (y in 0..7) {
            for (x in 0..7) {
                val indices = mutableListOf<Int>(y,x)
                val whiteAbleMove = mutableListOf<Boolean>()
                val blackAbleMove = mutableListOf<Boolean>()
                var validHorizIndices = mutableListOf<Int>()
                var validVerticalIndices = mutableListOf<Int>()

                if (piecesPositions[y][x] == "W") {
                    validHorizIndices = verHorizValidIndices(indices, firstPlayer)[0]
                    validVerticalIndices = verHorizValidIndices(indices, firstPlayer)[1]
                }
                if (piecesPositions[y][x] == "B") {
                    validHorizIndices = verHorizValidIndices(indices, secondPlayer)[0]
                    validVerticalIndices = verHorizValidIndices(indices, secondPlayer)[1]
                }
                stalemate = validHorizIndices.size == 0 && validVerticalIndices.size == 0
            }
        }

        if (stalemate) {
            println("Stalemate!")
            println("Bye")
            return
        }*/

        playerMove = getMoveFromPlayer(currentPlayer)
        if(playerMove == "exit") {
            println("Bye!")
            return
        }

        val initialMove = playerMove.substring(0,2)
        val nextMove = playerMove.substring(2)
        val initialMoveIndices = moveToIndex(move = initialMove)
        val nextMoveIndices = moveToIndex(move = nextMove)

        copyAll(piecesPositions, previousPiecesPositions)

        capture(currentPlayer, playerMove, initialMoveIndices, nextMoveIndices)

        //println(gameTurn)
        //chessBoard(previousPiecesPositions)
        //println("****")
        //chessBoard(piecesPositions)


        pieceCounter(piecesPositions)

        ++gameTurn
    }

}

fun pieceIsNotStucked(currentPiece: String): Boolean {
    for ( i in 0..7 ) {
        for ( j in 0..7 ) {
            if(currentPiece == "W") {
                if(i == 0) continue
                if(piecesPositions[i][j] == currentPiece) {
                    if (piecesPositions[i-1][j] == " ") {
                        return true
                    }
                    else {
                        if(j == 0) {
                            if (piecesPositions[i-1][j+1] == "B") {
                                return true
                            }
                        }
                        else if(j == 7) {
                            if (piecesPositions[i-1][j-1] == "B") {
                                return true
                            }
                        }
                        else {
                            if (piecesPositions[i-1][j+1] == "B") {
                                return true
                            }
                            if (piecesPositions[i-1][j-1] == "B") {
                                return true
                            }
                        }
                    }
                }
            }
            else {
                if(i == 7) continue
                if(piecesPositions[i][j] == currentPiece) {
                    if (piecesPositions[i+1][j] == " ") {
                        return true
                    }
                }
                else {
                    if(j == 0) {
                        if (piecesPositions[i+1][j+1] == "W") {
                            return true
                        }
                    }
                    else if(j == 7) {
                        if (piecesPositions[i+1][j-1] == "W") {
                            return true
                        }
                    }
                    else {
                        if (piecesPositions[i+1][j+1] == "W") {
                            return true
                        }
                        if (piecesPositions[i+1][j-1] == "W") {
                            return true
                        }
                    }
                }
            }
        }
    }
    return false
}

fun pieceCounter(values: MutableList<MutableList<String>>){
    var numWhites = 0
    var numBlacks = 0
    for (y in values) {
        for (x in y) {
            if (x == "W") ++numWhites
            if (x == "B") ++numBlacks
        }
    }
    numWhitePieces = numWhites
    numBlackPieces = numBlacks
}

fun capture(currentPlayer: String, playerMove: String, initialMoveIndices: MutableList<Int>, nextMoveIndices: MutableList<Int>) {
    if (getPlayerChessPiece(currentPlayer).first == "W"){
        piecesPositions[initialMoveIndices[0]][initialMoveIndices[1]] = " "
        piecesPositions[nextMoveIndices[0]][nextMoveIndices[1]] = "W"
    }
    if (getPlayerChessPiece(currentPlayer).first == "B"){
        piecesPositions[initialMoveIndices[0]][initialMoveIndices[1]] = " "
        piecesPositions[nextMoveIndices[0]][nextMoveIndices[1]] = "B"
    }


    if (getPlayerChessPiece(currentPlayer).first == "W") {
        when (initialMoveIndices[0]) {
            3 -> when (initialMoveIndices[1]) {
                0 -> if (enPassantIndices1) {
                    piecesPositions[nextMoveIndices[0] + 1][nextMoveIndices[1]] = " "
                }
                7 -> if (enPassantIndices2) {
                    piecesPositions[nextMoveIndices[0] + 1][nextMoveIndices[1]] = " "
                }
                else -> {
                    if (enPassantIndices1) {
                        piecesPositions[nextMoveIndices[0] + 1][nextMoveIndices[1]] = " "
                    }
                    if (enPassantIndices2) {
                        piecesPositions[nextMoveIndices[0] + 1][nextMoveIndices[1]] = " "
                    }
                }
            }
        }
    }
    else if (getPlayerChessPiece(currentPlayer).first == "B") {
        when (initialMoveIndices[0]) {
            4 -> when (initialMoveIndices[1]) {
                0 ->  if (enPassantIndices1) {
                    piecesPositions[nextMoveIndices[0]-1][nextMoveIndices[1]] = " "
                }
                7 -> if (enPassantIndices2) {
                    piecesPositions[nextMoveIndices[0]-1][nextMoveIndices[1]] = " "
                }
                else -> {
                    if (enPassantIndices1) {
                        piecesPositions[nextMoveIndices[0] - 1][nextMoveIndices[1]] = " "
                    }
                    if (enPassantIndices2) {
                        piecesPositions[nextMoveIndices[0] - 1][nextMoveIndices[1]] = " "
                    }
                }

            }
        }

    }
}


// 2 valid input => "exit", (regex)validMoveName
fun getMoveFromPlayer(currentPlayer: String): String {
    while(true) {
        println("$currentPlayer's turn:")

        var playerMove = readLine()!!

        while (!playerMove.matches(validMoveName)) {
            if (playerMove == "exit") return playerMove
            println("Invalid Input\n$currentPlayer' turn:")
            playerMove = readLine()!!
        }

        val initialMove = playerMove.substring(0,2)
        val nextMove = playerMove.substring(2)
        val initialMoveIndices = moveToIndex(initialMove)
        var playerPieceColor = getPlayerChessPiece(currentPlayer).second
        val playerPiece = getPlayerChessPiece(currentPlayer).first

        val checkInitialMoveSquare = checkPlayerPieceIfExists(initialMoveIndices, playerPiece)
        val nextPieceValid = checkNextMoveValid(initialMove, nextMove, currentPlayer)
        if(checkInitialMoveSquare && nextPieceValid) {
            return playerMove
        }
        else if(!checkInitialMoveSquare) {
            println("No ${getPlayerChessPiece(currentPlayer).second} pawn at ${playerMove.substring(0, 2)}")
        }
        else if(!nextPieceValid) {
            println("Invalid Input")
        }
    }
}

fun checkNextMoveValid(initialMove: String, nextMove: String, currentPlayer: String): Boolean {
    val initialMoveIndices = moveToIndex(move = initialMove)
    val nextMoveIndices = moveToIndex(move = nextMove)
    val validHorizontalIndices = mutableListOf(initialMoveIndices[1])

    val validVerticalMovesIndices: MutableList<Int> =  if (currentPlayer == firstPlayer) {
        if(initialMoveIndices[0] == firstPlayerStartPosition) mutableListOf(initialMoveIndices[0]-1, initialMoveIndices[0]-2)
        else mutableListOf(initialMoveIndices[0]-1)
    }
    else {
        if(initialMoveIndices[0] == secondPlayerStartPosition) mutableListOf(initialMoveIndices[0]+1, initialMoveIndices[0]+2)
        else mutableListOf(initialMoveIndices[0]+1)
    }
    when (currentPlayer) {
        firstPlayer -> when (initialMoveIndices[1]){
            0 -> if (piecesPositions[initialMoveIndices[0]-1][initialMoveIndices[1]+1] == "B") validHorizontalIndices.add(initialMoveIndices[1]+1)
            7 -> if (piecesPositions[initialMoveIndices[0]-1][initialMoveIndices[1]-1] == "B") validHorizontalIndices.add(initialMoveIndices[1]-1)
            else -> {
                if (piecesPositions[initialMoveIndices[0] - 1][initialMoveIndices[1] + 1] == "B") validHorizontalIndices.add(initialMoveIndices[1] + 1)
                if (piecesPositions[initialMoveIndices[0] - 1][initialMoveIndices[1] - 1] == "B") validHorizontalIndices.add(initialMoveIndices[1] - 1)
            }
        }

        secondPlayer -> when (initialMoveIndices[1]){
            0 -> if (piecesPositions[initialMoveIndices[0]+1][initialMoveIndices[1]+1] == "W") validHorizontalIndices.add(initialMoveIndices[1]+1)
            7 -> if (piecesPositions[initialMoveIndices[0]+1][initialMoveIndices[1]-1] == "W") validHorizontalIndices.add(initialMoveIndices[1]-1)
            else -> {
                if (piecesPositions[initialMoveIndices[0] + 1][initialMoveIndices[1] + 1] == "W") validHorizontalIndices.add(initialMoveIndices[1] + 1)
                if (piecesPositions[initialMoveIndices[0] + 1][initialMoveIndices[1] - 1] == "W") validHorizontalIndices.add(initialMoveIndices[1] - 1)
            }
        }
    }

    when (currentPlayer) {
        firstPlayer -> when (initialMoveIndices[0]) {
            3 -> when (initialMoveIndices[1]) {
                0 -> if (previousPiecesPositions[3 - 2][initialMoveIndices[1] + 1] == "B" && piecesPositions[3][initialMoveIndices[1] + 1] == "B") {
                        validHorizontalIndices.add(initialMoveIndices[1] + 1)
                        enPassantIndices1 = true
                    }
                7 -> if (previousPiecesPositions[3 - 2][initialMoveIndices[1] - 1] == "B" && piecesPositions[3][initialMoveIndices[1] - 1] == "B") {
                        validHorizontalIndices.add(initialMoveIndices[1] - 1)
                        enPassantIndices2 = true
                    }
                else -> {
                     if (previousPiecesPositions[3 - 2][initialMoveIndices[1] + 1] == "B" && piecesPositions[3][initialMoveIndices[1] + 1] == "B") {
                         validHorizontalIndices.add(initialMoveIndices[1] + 1)
                         enPassantIndices1 = true
                     }
                    if (previousPiecesPositions[3 - 2][initialMoveIndices[1] - 1] == "B" && piecesPositions[3][initialMoveIndices[1] - 1] == "B") {
                        validHorizontalIndices.add(initialMoveIndices[1] - 1)
                        enPassantIndices2 = true
                    }
                }
            }
        }

        secondPlayer -> when (initialMoveIndices[0]) {
            4 -> when (initialMoveIndices[1]) {
                0 -> if (previousPiecesPositions[4 + 2][initialMoveIndices[1] + 1] == "W" && piecesPositions[4][initialMoveIndices[1] + 1] == "W") {
                        validHorizontalIndices.add(initialMoveIndices[1] + 1)
                        enPassantIndices1 = true
                    }
                7 -> if (previousPiecesPositions[4 + 2][initialMoveIndices[1] - 1] == "W" && piecesPositions[4][initialMoveIndices[1] - 1] == "W") {
                        validHorizontalIndices.add(initialMoveIndices[1] - 1)
                        enPassantIndices2 = true
                    }
                else -> {
                    if (previousPiecesPositions[4 + 2][initialMoveIndices[1] + 1] == "W" && piecesPositions[4][initialMoveIndices[1] + 1] == "W") {
                        validHorizontalIndices.add(initialMoveIndices[1] + 1)
                        enPassantIndices1 = true
                        }
                    if (previousPiecesPositions[4 + 2][initialMoveIndices[1] - 1] == "W" && piecesPositions[4][initialMoveIndices[1] - 1] == "W") {
                        validHorizontalIndices.add(initialMoveIndices[1] - 1)
                        enPassantIndices2 = true
                        }
                    }

                }
            }
        }


    val nextMoveSquareAvaiable: Boolean = piecesPositions[nextMoveIndices[0]][initialMoveIndices[1]] == " "
    val nextMoveIsValid = validVerticalMovesIndices.contains(nextMoveIndices[0]) && validHorizontalIndices.contains(nextMoveIndices[1])
    return nextMoveIsValid && nextMoveSquareAvaiable
}

fun getPlayerChessPiece(currentPlayer: String): Pair<String, String> {

    return if(currentPlayer == firstPlayer) Pair("W", "white") else Pair("B", "black")
}

fun checkPlayerPieceIfExists(initialMoveIndices: MutableList<Int>,
                             playerPiece: String): Boolean {
    return piecesPositions[initialMoveIndices[0]][initialMoveIndices[1]] == playerPiece
}

fun startGame(values: MutableList<MutableList<String>>) {
    for (i in 0..7){
        values[1][i] = "B"
    }

    for ( i in 0..7){
        values[6][i] = "W"
    }
}

fun chessBoard(values: MutableList<MutableList<String>>) {
    val horizontalShape = "  +---+---+---+---+---+---+---+---+"

    println(horizontalShape)
    var lineNumber = 8

    for (y in values) {
        print("$lineNumber ")
        for (x in y) {
            print("| $x ")
        }
        println("|")
        println(horizontalShape)
        lineNumber -= 1
    }
    println("    a   b   c   d   e   f   g   h")
}

fun moveToIndex(move: String): MutableList<Int> {
    val ranks1 = mutableListOf("a", "b", "c", "d", "e", "f", "g", "h")
    val files1 = mutableListOf("8", "7", "6", "5", "4", "3", "2", "1")
    val moveIndices = mutableListOf<Int>()
    moveIndices.add(files1.indexOf(move[1].toString()))
    moveIndices.add(ranks1.indexOf(move[0] .toString()))

    return moveIndices
}

fun copyAll(fromList: MutableList<MutableList<String>>, toList: MutableList<MutableList<String>>) {
    for (i in 0 until toList.size) {
        for (j in 0 until toList[i].size) {
            toList[i][j] = fromList[i][j]
        }
    }
}