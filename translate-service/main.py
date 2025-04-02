from fastapi import FastAPI, APIRouter
from translate import Translator
from contextlib import asynccontextmanager
import py_eureka_client.eureka_client as eureka_client
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@asynccontextmanager
async def lifespan(app: FastAPI):
    try:
        await eureka_client.init_async(
            eureka_server="http:localhost:8761/eureka",
            app_name="translate-service",
            instance_port=8443,
            instance_host="localhost"
        )
        logger.info("Successfully registered with Eureka")
    except Exception as e:
        logger.error(f"Failed to register with Eureka: {e}")
    yield

    try:
        await eureka_client.stop_async()
        logger.info("Successfully deregistered from Eureka")
    except Exception as e:
        logger.error(f"Failed to deregister from Eureka: {e}")


class TranslateUtil:
    @staticmethod
    def translate(from_lang: str, to_lang: str, message: str) -> str:
        try:
            translator = Translator(from_lang=from_lang, to_lang=to_lang)
            return translator.translate(message)
        except Exception as e:
            logger.error(f"Translation error: {e}")
            raise


app = FastAPI(lifespan=lifespan)
router = APIRouter(prefix="/api/v1")


@router.get("/translate", tags=["translation"])
async def translate_text(from_lang: str, to_lang: str, message: str):
    try:
        translated_text = TranslateUtil.translate(from_lang, to_lang, message)
        return {"translated_text": translated_text}
    except Exception as e:
        logger.error(f"API error: {e}")
        return {"error": str(e)}, 500


app.include_router(router)