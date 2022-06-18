from pathlib import Path
from typing import Iterator, List

import pandas as pd


class FlatColumnDatabase:
    def __init__(self, path_resource: Path) -> None:
        self.data = self.__read(path_resource)

    def iterate_transposed_groups(self, s_group_delimiters: pd.Series) -> Iterator[pd.Series]:
        data = [self.data[0].replace('\n', '')]

        for line in self.data[1:]:
            if self.__is_tier(line, s_group_delimiters):
                yield pd.Series(data, name='preprocessed_data_group')

                data = [line.replace('\n', '')]

            else:
                data.append(line.replace('\n', ''))

    def __is_tier(self, line: str, values: pd.Series) -> bool:
        return any([value in line for value in values])

    def __read(self, path_resource: Path) -> List[str]:
        with open(path_resource, 'r', encoding='utf8') as f:
            return f.readlines()
