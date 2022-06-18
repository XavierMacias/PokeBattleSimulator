from pathlib import Path

import pandas as pd

from data_gathering.flat_column_database import FlatColumnDatabase


if __name__ == '__main__':
    path_database = Path('resources\\first_party_data\\pokemon_database.txt')
    path_tiers = Path('resources\\first_party_data\\tiers.txt')
    
    tiers = pd.read_csv(path_tiers, header=None).squeeze()

    flat_column_database = FlatColumnDatabase(path_database)
    transposed_groups = flat_column_database.iterate_transposed_groups(s_group_delimiters=tiers)

    for group in transposed_groups:
        print(group)
