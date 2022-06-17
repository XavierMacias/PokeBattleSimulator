from pathlib import Path
import re
from typing import Iterator, List

import pandas as pd


class LinearDatabaseGather:
    def __init__(self) -> None:
        raw_strings = self.extract_raw_strings()

        for values in raw_strings:
            print(values)

            for value in values:
                splitted = value.replace('\n', '')
                split = re.findall('[A-Z][^A-Z]*', splitted)

                print(split)

            exit()

    def extract_raw_strings(self) -> Iterator[List[str]]:
        path_database = Path('resources\\first_party_data\\pokemon_database.txt')

        with open(path_database, 'r', encoding='utf8') as f:
            lines = f.readlines()

            for i in range(0, len(lines), 9):
                yield lines[i: i+9]


