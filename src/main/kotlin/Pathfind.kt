import java.util.*

data class Path<N>(val length: Double, val nodes: List<N>)
data class PathData<N>(var visitedNodes: Map<N, Path<N>> = mapOf())

fun <N> findPath(start: N, isEnd: (node: N) -> Boolean, data: PathData<N> = PathData(mapOf()), edgesForNode: (node: N) -> List<Pair<N, Double>>): Path<N>? {
    val distancesToNodes = mutableMapOf(start to Path(0.0, listOf(start)))
    val nodesToVisit: SortedSet<N> = TreeSet { left, right ->
        if (left == right) {
            return@TreeSet 0
        }

        val comparison = (distancesToNodes[left]!!.length - distancesToNodes[right]!!.length).toInt()
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
            val newLength = shortestPath.length + length
            if (pathForNode == null || pathForNode.length > newLength) {
                if (distancesToNodes.contains(node)) {
                    // Because nodesToVisit is ordered by distancesToNodes, its invariants break when
                    //   a shorter path is found later on. In this case, we have to remove the node and readd it
                    nodesToVisit.remove(node)
                }
                val newPath = Path(shortestPath.length + length, shortestPath.nodes + node)
                distancesToNodes[node] = newPath
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

    return findPath(start = start, isEnd = isEnd, data = data, edgesForNode = modifiedEdgesForNode)
}