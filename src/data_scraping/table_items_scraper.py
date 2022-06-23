from typing import Iterable, Iterator

import pandas as pd
from bs4 import BeautifulSoup, Tag


class TableItemsScraper:
    @classmethod
    def scrap_row_image_attribute(cls, soup: BeautifulSoup, image_attribute: str)-> Iterator[Iterable[pd.Series]]:
        return (
            [cls.__scrap_tag_attribute(tag, image_attribute)]
            for tag in soup.find_all(class_='col typecol')
        )

    @classmethod
    def scrap_row_text(cls, soup: BeautifulSoup, class_row_items: str) -> Iterator[str]:
        return (result.text for result in soup.find_all(class_=class_row_items))

    @classmethod
    def __scrap_tag_attribute(cls, tag: Tag, image_attribute: str) -> pd.Series:
        attributes = [image.attrs[image_attribute] for image in tag.find_all('img')]

        return pd.Series(attributes)
