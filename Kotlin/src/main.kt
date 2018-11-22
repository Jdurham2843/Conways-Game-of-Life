fun main(args: Array<String>) {
    val (height, width) = getSizeArguments(args)

    val initialGrid : Grid = createInitialGrid(height, width)

    startLife(initialGrid)
}

fun getSizeArguments(args: Array<String>) : Pair<Int, Int> {
    /* TODO: get command line arguments for life dimensions */
    return Pair(20, 20)
}

fun startLife(initialGrid: Grid) {
    var generation = 0
    initialGrid.print(generation)

    var grid = initialGrid
    while(true) {
        Thread.sleep(1000)
        clearScreen()
        generation++
        grid = grid.nextGeneration()
        grid.print(generation)
    }
}

fun clearScreen() {
    val os = System.getProperty("os.name")

    if (os.contains("Windows")) {
        Runtime.getRuntime().exec("cls")
    } else {
        Runtime.getRuntime().exec("clear")
    }
}
