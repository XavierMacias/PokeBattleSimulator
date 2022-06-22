import itertools
import re
from typing import Iterator

import pandas as pd


class PreprocessedRowBuilder:
    @classmethod
    def build_preprocessed_rows(cls, iterator_rows: Iterator[str]) -> Iterator[pd.DataFrame]:
        for text in iterator_rows:
            data = text.split('  ')
            if len(data) == 3:
                undivised_data = dict(zip(['tier', 'name'], data[0:2]))
                df_undivised_data = pd.DataFrame.from_dict(undivised_data, orient='index').T
                
                habilities, stats = data[2].split('HP')
                stats = 'HP' + stats

                df_habilites = cls.parse_habilites(habilities)
                df_stats = cls.parse_stats(stats)

                df_total = pd.concat([df_undivised_data, df_habilites, df_stats], axis='columns')

                yield df_total

    @classmethod
    def parse_habilites(cls, row_habilities):
        parsed_habilities = cls.__camel_case_split(row_habilities)
        hability_types = ['hability_1', 'hability_2', 'hidden_hability']

        habilities_mapping = dict(itertools.zip_longest(hability_types, parsed_habilities, fillvalue=None))

        df = pd.DataFrame.from_dict(habilities_mapping, orient='index').T
        
        return df

    @classmethod
    def __camel_case_split(cls, text: str):
        matches = re.finditer('.+?(?:(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|$)', text)
        return [m.group(0) for m in matches]

    @classmethod
    def parse_stats(cls, row_stats) -> pd.DataFrame:
        stats = dict(re.findall(r'(\w+?)(\d+)', row_stats))
        df_stats = pd.DataFrame.from_dict(stats, orient='index').T.convert_dtypes()

        return df_stats
