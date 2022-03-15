from math import sqrt, atan2

def solution(dimensions, your_position, trainer_position, distance):
    trainer_reflections = get_reflections(dimensions, your_position, trainer_position, distance)
    you_reflections = get_reflections(dimensions, your_position, your_position, distance)
    
    distinct_directions = {}
    for _, t_pos in enumerate(trainer_reflections):
        if (get_distance(your_position, t_pos) > distance): continue
        x_t_pos, y_t_pos = t_pos[0] - your_position[0], t_pos[1] - your_position[1]
        bearing = get_bearing(x_t_pos, y_t_pos)
        if (bearing not in distinct_directions):
            distinct_directions[bearing] = t_pos
            
    for _, y_pos in enumerate(you_reflections):
        if (y_pos == tuple(your_position)): continue 
        if (get_distance(your_position, y_pos) >= distance): continue
        x_y_pos, y_y_pos = y_pos[0] - your_position[0], y_pos[1] - your_position[1]
        bearing = get_bearing(x_y_pos, y_y_pos)
        if (bearing in distinct_directions):
            t_pos = distinct_directions[bearing]
            if (get_distance(your_position, y_pos) < get_distance(your_position, t_pos)):
                del distinct_directions[bearing]
                
    return len(distinct_directions)
    
def get_distance(a, b):
    return sqrt((b[0] - a[0]) ** 2 + (b[1] - a[1]) ** 2)
    
def get_bearing(a, b):
    return atan2(a, b)
    
def get_reflections(dimensions, position_1, position_2, distance):
    positions = []
    
    x_range = int((position_1[0] + distance) / dimensions[0]) + 1
    y_range = int((position_1[1] + distance) / dimensions[1]) + 1
    
    y_temp = position_2[1]
    for i in range(y_range):
        x_temp = position_2[0]
        if (i != 0):
            if (i % 2 == 0):
                y_temp += 2 * position_2[1] 
            else:
                y_temp += 2 * (dimensions[1] - position_2[1])
        for j in range(x_range):
            positions.append((x_temp, y_temp))
            positions.append((x_temp, -y_temp))
            positions.append((-x_temp, -y_temp))
            positions.append((-x_temp, y_temp))
            if (j % 2 == 0):
                x_temp += 2 * (dimensions[0] - position_2[0])
            else:
                x_temp += 2 * position_2[0]
        
    return positions

#print(solution([3,2],[1,1],[2,1],4))#7
#print(solution([42,59],[34,44],[6,34],5000))#30904
print(solution([2,5],[1,2],[1,4],11))#27
#print(solution([10,10],[4,4],[3,3],5000))#739323
#print(solution([23,10],[6,4],[3,2],23))#8
#print(solution([1250,1250],[1000,1000],[500,400],10000))#196