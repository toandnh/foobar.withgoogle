import sys

infinity = sys.maxint

def solution(entrances, exits, path):
    graph = make_graph(entrances, exits, path)
    return max_flow_preflow_fifo_selection(0, len(graph) - 1, graph)
    
def make_graph(entrances, exits, path):
    graph = [[0 for x in range(len(path) + 2)] for y in range(len(path) + 2)]
    #make super source.
    for i in range(len(entrances)):
        graph[0][entrances[i] + 1] = infinity
    #make super sink.
    for i in range(len(exits)):
        graph[exits[i] + 1][len(graph) - 1] = infinity
    #add the rest of the elements.
    for i in range(len(path)):
        for j in range(len(path)):
            graph[i + 1][j + 1] = path[i][j]
    return graph
    
def max_flow_preflow_fifo_selection(s, t, capacity):
    n = len(capacity)
    
    preflow = [[0 for x in range(n)] for y in range(n)]
    excess = [0 for _ in range(n)]
    height = [0 for _ in range(n)]
    height[s] = n
    
    queue = []
    
    for i in range(n):
        preflow[s][i] = capacity[s][i]
        preflow[i][s] = -capacity[s][i]
        excess[i] = capacity[s][i]
        if i != t and capacity[s][i] > 0:
            queue.append(i)
            
    while len(queue) > 0:
        i = queue.pop(0)
        for j in range(n):
            if excess[i] > 0 and capacity[i][j] - preflow[i][j] > 0 and height[i] == height[j] + 1:
                #push.
                delta = min(excess[i], capacity[i][j] - preflow[i][j])
                preflow[i][j] += delta
                preflow[j][i] -= delta
                excess[i] -= delta
                excess[j] += delta
                if j != s and j != t and (j not in queue):
                    queue.append(j)
        if excess[i] > 0:
            #relabel.
            height[i] = infinity
            for j in range(n):
                if capacity[i][j] - preflow[i][j] > 0 and height[i] > height[j] + 1:
                    height[i] = height[j] + 1
            queue.append(i)
            
    flow = 0
    for i in range(n):
        flow += preflow[s][i]
    return flow
    
print(solution([0,1], [4,5], [[0,0,4,6,0,0],[0,0,5,2,0,0],[0,0,0,0,4,4],[0,0,0,0,6,6],[0,0,0,0,0,0],[0,0,0,0,0,0]]))
print(solution([0], [3], [[0,7,0,0],[0,0,6,0],[0,0,0,8],[9,0,0,0]]))