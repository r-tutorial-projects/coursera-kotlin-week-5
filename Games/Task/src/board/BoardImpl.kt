package board

import board.Direction.*
import java.util.*
import kotlin.collections.HashMap

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)


class GameBoardImpl<T>(private val w: Int) : SquareBoardImpl(w), GameBoard<T> {
    private val contents = HashMap<Cell, T?>()

    init {
        for (cell in super.board.values) {
            contents[cell] = null
        }
    }

    override fun get(cell: Cell): T? {
        return contents[cell]
    }

    override fun set(cell: Cell, value: T?) {
        contents[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return contents.filterValues(predicate).keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return contents.filterValues(predicate).keys.elementAtOrNull(0)
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return contents.filterValues(predicate).any()
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return contents.all { (_, value) -> predicate(value) }
    }

}

open class SquareBoardImpl(override val width: Int) : SquareBoard {
    protected val board: MutableMap<Pair<Int, Int>, Cell> = HashMap()

    init {
        for (i in 1..this.width) {
            for (j in 1..this.width) {
                board[Pair(i, j)] = Cell(i, j)
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return board[Pair(i, j)]
    }

    override fun getCell(i: Int, j: Int): Cell {
        return when {
            i > width || j > width -> throw IllegalArgumentException("not in range")
            else -> getCellOrNull(i, j)!!
        }
    }

    override fun getAllCells(): Collection<Cell> {
        return board.values
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val cells = LinkedList<Cell>()
        for (j in jRange) {
            if (j <= width) cells.add(getCell(i, j))
        }
        return cells
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val cells = LinkedList<Cell>()
        for (i in iRange) {
            if (i <= width) cells.add(getCell(i, j))
        }
        return cells
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> getCellOrNull(i - 1, j)
            DOWN -> getCellOrNull(i + 1, j)
            RIGHT -> getCellOrNull(i, j + 1)
            LEFT -> getCellOrNull(i, j - 1)
        }
    }

}
