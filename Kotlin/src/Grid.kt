import java.lang.StringBuilder

enum class CellStatus(val statusSymbol: Char) {
    LIVE('X'),
    DEAD('.');

    override fun toString() : String = this.statusSymbol.toString()
}

typealias Grid = MutableList<MutableList<CellStatus>>

fun Grid.print(generation: Int) {
    val stringBuilder = StringBuilder()
    stringBuilder.append("Generation: ${generation}\n")

    this.forEach { row ->
        stringBuilder.append("| ")

        row.forEach { cellStatus ->
            stringBuilder.append(cellStatus.statusSymbol)
            stringBuilder.append(' ')
        }
        stringBuilder.append("|\n")
    }
    println(stringBuilder.toString())
}

fun createInitialGrid(height: Int = 20, width: Int = 20) : Grid {
    val grid: MutableList<MutableList<CellStatus>> = ArrayList()

    (0..height).forEach { w ->
        val row = ArrayList<CellStatus>()
        for (h in 0..width) {
            row.add(CellStatus.DEAD)
        }
        grid.add(row)
    }

    grid.addInitialValues()
    return grid
}

private fun Grid.addInitialValues() {
    val heightMidPoint: Int = this.size / 2
    val widthMidPoint: Int = this.size / 2

    this[heightMidPoint][widthMidPoint] = CellStatus.LIVE
    this[heightMidPoint][widthMidPoint - 1] = CellStatus.LIVE
    this[heightMidPoint][widthMidPoint + 1] = CellStatus.LIVE
}

fun Grid.nextGeneration() : Grid {
    val newGrid = this.map { row -> row.map { cs ->  cs }.toCollection(ArrayList()) }.toCollection(ArrayList())

    for ((hIndex, column) in newGrid.withIndex()) {
       for ((wIndex, _) in column.withIndex())  {
           newGrid[hIndex][wIndex] = this.determineLivingStatus(hIndex, wIndex)
       }
    }

    return this
}

private fun Grid.determineLivingStatus(y: Int, x: Int) : CellStatus{
    return if (y == 0 && x == 0)
        this.topLeftLifeCheck(y, x)
    else if (y == this.size - 1 && x == 0)
        this.bottomLeftLifeCheck(y, x)
    else if (y == 0 && x == this[0].size - 1)
        this.topRightLifeCheck(y, x)
    else if (y == this.size - 1 && x == this[0].size - 1)
        this.bottomRightLifeCheck(y, x)
    else
        this.middleLifeCheck(y, x)
}

private fun Grid.topLeftLifeCheck(y: Int, x: Int) : CellStatus {
    var liveNeighborCount = 0

    if (this[y + 1][x] == CellStatus.LIVE)
        liveNeighborCount++
    if (this[y + 1][x + 1] == CellStatus.LIVE)
        liveNeighborCount++
    if (this[y][x + 1] == CellStatus.LIVE)
        liveNeighborCount++

    val currentStatus = this[y][x]
    return makeDetermination(currentStatus, liveNeighborCount)
}

private fun Grid.bottomLeftLifeCheck(y: Int, x: Int) : CellStatus {
    var liveNeighborCount = 0

    if (this[y - 1][x] == CellStatus.LIVE)
        liveNeighborCount++
    if (this[y - 1][x + 1] == CellStatus.LIVE)
        liveNeighborCount++
    if (this[y][x + 1] == CellStatus.LIVE)
        liveNeighborCount++

    val currentStatus = this[y][x]
    return makeDetermination(currentStatus, liveNeighborCount)
}

private fun Grid.topRightLifeCheck(y: Int, x: Int) : CellStatus {
    var liveNeighborCount = 0

    if (this[y + 1][x] == CellStatus.LIVE)
        liveNeighborCount++
    if (this[y + 1][x - 1] == CellStatus.LIVE)
        liveNeighborCount++
    if (this[y][x - 1] == CellStatus.LIVE)
        liveNeighborCount++

    val currentStatus = this[y][x]
    return makeDetermination(currentStatus, liveNeighborCount)
}

private fun Grid.bottomRightLifeCheck(y: Int, x: Int) : CellStatus {
    var liveNeighborCount = 0

    if (this[y - 1][x] == CellStatus.LIVE ||
        this[y - 1][x - 1] == CellStatus.LIVE ||
        this[y][x - 1] == CellStatus.LIVE) {
        liveNeighborCount++
    }

    val currentStatus = this[y][x]
    return makeDetermination(currentStatus, liveNeighborCount)
}

private fun Grid.middleLifeCheck(y: Int, x: Int) : CellStatus {
    var liveNeighborCount = 0

    if (y != 0 && x != 0 && this[y - 1][x - 1] == CellStatus.LIVE)
        liveNeighborCount++
    if (y != 0 && this[y - 1][x] == CellStatus.LIVE)
        liveNeighborCount++
    if (y != 0 && x != this[0].size -1  && this[y - 1][x + 1] == CellStatus.LIVE)
        liveNeighborCount++
    if (x != 0 && this[y][x - 1] == CellStatus.LIVE)
        liveNeighborCount++
    if (x != this[0].size -1  && this[y][x + 1] == CellStatus.LIVE)
        liveNeighborCount++
    if (x != 0 && y != this.size - 1  && this[y + 1][x - 1] == CellStatus.LIVE)
        liveNeighborCount++
    if (y != this.size - 1 && this[y + 1][x] == CellStatus.LIVE)
        liveNeighborCount++
    if (y != this.size - 1 && x != this[0].size - 1 && this[y + 1][x + 1] == CellStatus.LIVE)
        liveNeighborCount++

    val currentStatus = this[y][x]
    return makeDetermination(currentStatus, liveNeighborCount)
}

private fun makeDetermination(currentStatus: CellStatus, liveNeighborCount: Int) : CellStatus {
    return if (liveNeighborCount == 3 && currentStatus == CellStatus.DEAD)
        CellStatus.LIVE
    else if ((4 > liveNeighborCount  && liveNeighborCount > 1) && currentStatus == CellStatus.LIVE)
        CellStatus.LIVE
    else if ((liveNeighborCount < 2 || liveNeighborCount > 3) && currentStatus == CellStatus.LIVE)
        CellStatus.DEAD
    else
        CellStatus.DEAD
}
