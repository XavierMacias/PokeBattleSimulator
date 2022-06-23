import re

import pandas as pd


class StringPatternSearching:
    @classmethod
    def extract_numbers(cls, text: str) -> pd.Series:
        pairs = re.findall(r'(\w+?)(\d+)', text)
        numbers = [int(value) for _, value in pairs]

        return pd.Series(numbers)

    @classmethod
    def split_by_camelcase(cls, text: str) -> pd.Series:
        matches = re.finditer('.+?(?:(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|$)', text)
        return pd.Series([match.group(0) for match in matches])
