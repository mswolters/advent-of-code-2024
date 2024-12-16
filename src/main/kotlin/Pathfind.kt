import day.Day16
import java.util.*

data class Path<N>(val nodes: List<N>, val length: Double)
data class PathData<N>(var visitedNodes: Map<N, Path<N>> = mapOf())

fun <N> findPath(start: N, isEnd: (node: N) -> Boolean, data: PathData<N> = PathData(mapOf()), edgesForNode: (node: N) -> List<Pair<N, Double>>): Path<N>? {
    val distancesToNodes = mutableMapOf(start to Path(listOf(start), 0.0))
    val nodesToVisit: SortedSet<N> = TreeSet { left, right ->
        if (left == right) {
            return@TreeSet 0
        }

        val comparison = (distancesToNodes[left]!!.length - distancesToNodes[right]!!.length).toInt()
        if ((left as Day16.PathLocation).coordinate == Rectangle.Coordinate(11, 118) || (right as Day16.PathLocation).coordinate == Rectangle.Coordinate(11, 118)) {
            println()
        }
        if (comparison == 0) {
            if (left.hashCode() > right.hashCode()) 1 else -1
        } else {
            comparison
        }
    }
    var (shortestNode, shortestPath) = distancesToNodes.minBy { (_, path) -> path.length }
    while (!(isEnd(shortestNode))) {
        val edges = edgesForNode(shortestNode)
        edges.forEach { (node, length) ->
            val pathForNode = distancesToNodes[node]
            if (pathForNode == null || pathForNode.length > shortestPath.length + length) {
                if (distancesToNodes.contains(node)) {
                    // Because nodesToVisit is ordered by distancesToNodes, its invariants break when
                    //   a shorter path is found later on. In this case, we have to remove the node and readd it
                    nodesToVisit.remove(node)
                }
                distancesToNodes[node] = Path(shortestPath.nodes + node, shortestPath.length + length)
                nodesToVisit.add(node)
            } else {
                //println("Skipping")
            }
        }
        if (nodesToVisit.isEmpty()) {
            data.visitedNodes = distancesToNodes
            return null
        }
        shortestNode = nodesToVisit.first()
        nodesToVisit.remove(shortestNode)
        shortestPath = distancesToNodes[shortestNode]!!
    }
    data.visitedNodes = distancesToNodes
    return shortestPath
}

fun <N> findPathAStar(start: N, isEnd: (node: N) -> Boolean, data: PathData<N> = PathData(mapOf()), edgesForNode: (node: N) -> List<Pair<N, Double>>, potential: (node: N) -> Double): Path<N>? {
    val modifiedEdgesForNode = { forNode: N ->
        edgesForNode(forNode)
            .map { (node, length) -> Pair(node, length + potential(node) - potential(forNode)) }
    }

    return findPath(start, isEnd, data, modifiedEdgesForNode)
}