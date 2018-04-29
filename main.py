import argparse
import time
import os

LIVE_CELL_MARKER = 'X'
DEAD_CELL_MARKER = '.'
CLEAR_SCREEN_COMMAND = 'cls' if os.name == 'nt' else 'clear'

def parse_cli_arguments():
    parser = argparse.ArgumentParser()
    parser.add_argument('--height', help='specifies the height of the grid')
    parser.add_argument('--width', help='specifies the width of the grid')
    args = parser.parse_args()
    return args

def print_grid(grid, generation):
    os.system(CLEAR_SCREEN_COMMAND)
    print('Generation: {generation}'.format(generation=generation))
    for row in grid:
        print(row)

def init_grid(height, width):
    grid = [['.' for x in range(width)] for x in range(height)]
    grid = add_initial_live_cells(grid)
    return grid

def add_initial_live_cells(grid):
    # creating a blinker pattern
    height_mid_point = int(len(grid) / 2)
    width_mid_point = int(len(grid) / 2)

    grid[height_mid_point][width_mid_point] = LIVE_CELL_MARKER
    grid[height_mid_point][width_mid_point - 1] = LIVE_CELL_MARKER
    grid[height_mid_point][width_mid_point + 1] = LIVE_CELL_MARKER
    return grid

def start_life(grid):
    generation = 0
    while True:
        time.sleep(.5)
        generation = generation + 1
        grid = mutate_grid(grid)
        print_grid(grid, generation)

def mutate_grid(grid):
    height = len(grid)
    width = len(grid[0])

    new_grid = [[DEAD_CELL_MARKER for x in range(width)] for y in range(height)]
    for h in range(height - 1):
        for w in range(width - 1):
            new_grid[h][w] = determine_living_status(grid, h, w)

    return new_grid

def determine_living_status(grid, y, x):
    if y == 0 and x == 0:
        return top_left_life_check(grid, y, x)
    elif y == len(grid) - 1 and x == 0:
        return bottom_left_life_check(grid, y, x)
    elif y == 0 and x == len(grid[0]) - 1:
        return top_right_life_check(grid, y, x)
    elif y == len(grid) - 1 and x == len(grid[0]) - 1:
        return bottom_right_life_check(grid, y, x)
    else:
        return middle_life_check(grid, y, x)

def top_left_life_check(grid, y, x):
    live_neighbor_count = 0
    if grid[y + 1][x] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y + 1][x + 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y][x + 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1

    current_state = grid[y][x]
    return make_determination(current_state, live_neighbor_count)

def bottom_left_life_check(grid, y, x):
    live_neighbor_count = 0
    if grid[y - 1][x] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y - 1][x + 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y][x + 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1

    current_state = grid[y][x]
    return make_determination(current_state, live_neighbor_count)

def top_right_life_check(grid, y, x):
    live_neighbor_count = 0
    if grid[y + 1][x] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y + 1][x - 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y][x - 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1

    current_state = grid[y][x]
    return make_determination(current_state, live_neighbor_count)

def bottom_right_life_check(grid, y, x):
    live_neighbor_count = 0
    if grid[y - 1][x] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y - 1][x - 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y][x - 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1

    current_state = grid[y][x]
    return make_determination(current_state, live_neighbor_count)

def middle_life_check(grid, y, x):
    live_neighbor_count = 0
    if grid[y - 1][x - 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y - 1][x] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y - 1][x + 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y][x - 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y][x + 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y + 1][x - 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y + 1][x] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1
    if grid[y + 1][x + 1] == LIVE_CELL_MARKER:
        live_neighbor_count = live_neighbor_count + 1

    current_state = grid[y][x]
    return make_determination(current_state, live_neighbor_count)

def make_determination(current_state, live_neighbor_count):
    if live_neighbor_count == 3 and current_state == DEAD_CELL_MARKER:
        return LIVE_CELL_MARKER
    elif (4 > live_neighbor_count > 1) and current_state == LIVE_CELL_MARKER:
        return LIVE_CELL_MARKER
    elif (live_neighbor_count < 2 or live_neighbor_count > 3) and current_state == LIVE_CELL_MARKER:
        return DEAD_CELL_MARKER
    else:
        return DEAD_CELL_MARKER

if __name__ == '__main__':
    # TODO: add ability to provide seed file
    args = parse_cli_arguments()
    grid = init_grid(int(args.height), int(args.width)) if args.height and args.width else init_grid(20, 20)
    
    start_life(grid)

