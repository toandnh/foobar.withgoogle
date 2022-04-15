def solution(g):
    grid = {}
    
    for i in range(len(g[0])):
        if i == 0:
            for col in generate_cols(g, i):
                if col[1] in grid:
                    grid[col[1]] = grid.get(col[1]) + 1
                    continue
                grid[col[1]] = 1
            continue
        
        cols = {}
        for col in generate_cols(g, i):
            if col[0] in cols:
                cols.get(col[0]).append(col[1])
                continue
            cols[col[0]] = [col[1]]
        temp_grid = {}
        for key, value in grid.items():
            if key in cols:
                for col_value in cols.get(key):
                    if col_value in temp_grid:
                        temp_grid[col_value] = temp_grid.get(col_value) + value
                        continue
                    temp_grid[col_value] = value
        grid = temp_grid
    
    return sum(grid.get(key) for key, _ in grid.items())
    
def generate_cols(g, index_col):
    cols = {}
    
    for i in range(len(g)):
        if i == 0: 
            for block in get_blocks(not g[i][index_col]):
                temp_list = []
                temp_list.append(block[2])
                temp_list.append(block[3])
                if block[1] in cols:
                    cols.get(block[1]).append(temp_list)
                    continue
                cols[block[1]] = [temp_list]
            continue
        
        blocks = {}
        for block in get_blocks(not g[i][index_col]):
            if block[0] in blocks:
                blocks.get(block[0]).append(block[1])
                continue
            blocks[block[0]] = [block[1]]
        temp_cols = {}
        for key, values in cols.items():
            if key in blocks:
                for value in values:
                    for block_value in blocks.get(key):
                        temp_list = updated_values(value, block_value)
                        if block_value in temp_cols:
                            temp_cols.get(block_value).append(temp_list)
                            continue
                        temp_cols[block_value] = [temp_list]
        cols = temp_cols
        
    return [col for _, values in cols.items() for col in values]
                            
def updated_values(values, value):
    return [values[0] * 2 + dict.get(value)[0], values[1] * 2 + dict.get(value)[1]]
    
def get_blocks(empty):
    return EMPTY_BLOCK if empty else HAS_GAS_BLOCK
    
dict = {
    0 : (0, 0),
    1 : (0, 1),
    2 : (1, 0),
    3 : (1, 1)
}
    
HAS_GAS_BLOCK = ((0, 1, 0, 1), (0, 2, 1, 0), (1, 0, 0, 2), (2, 0, 2, 0))

EMPTY_BLOCK = ((0, 0, 0, 0), (3, 3, 3, 3), (3, 0, 2, 2), (2, 2, 3, 0), (2, 1, 2, 1), (1, 2, 1, 2), (1, 1, 0, 3), (0, 3, 1, 1), (3, 2, 3, 2), (3, 1, 2, 3), (2, 3, 3, 1), (1, 3, 1, 3))

#g = [[True, False, True], [False, True, False], [True, False, True]]
#g = [[True, False, True, False, False, True, True, True], [True, False, True, False, False, False, True, False], [True, True, True, False, False, False, True, False], [True, False, True, False, False, False, True, False], [True, False, True, False, False, True, True, True]]
g = [[True, True, False, True, False, True, False, True, True, False], [True, True, False, False, False, False, True, True, True, False], [True, True, False, False, False, False, False, False, False, True], [False, True, False, False, False, False, True, True, False, False]]

print(solution(g))