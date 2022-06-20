import time

from selenium import webdriver


class DynamicContentLoader:
    @classmethod
    def load_html(cls, url: str, web_driver: webdriver.Chrome, load_time: int=5) -> str:
        with web_driver:
            web_driver.get(url)
            time.sleep(load_time)

            return web_driver.page_source
