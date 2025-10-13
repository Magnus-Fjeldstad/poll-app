import pika
import requests
import json
import sys
from requests.auth import HTTPBasicAuth

RABBIT_HOST = "localhost"
AMQP_PORT = 5672
HTTP_PORT = 15672
USER = "guest"
PASS = "guest"

def check_amqp_connection():
    print(f"Connecting to RabbitMQ via AMQP ({RABBIT_HOST}:{AMQP_PORT})...")
    params = pika.ConnectionParameters(
        host=RABBIT_HOST,
        port=AMQP_PORT,
        credentials=pika.PlainCredentials(USER, PASS)
    )
    connection = pika.BlockingConnection(params)
    channel = connection.channel()
    channel.queue_declare(queue="test_check_queue", durable=False)

    msg = {"status": "ping", "from": "python"}
    channel.basic_publish(exchange="", routing_key="test_check_queue", body=json.dumps(msg))
    print("📤 Sent test message.")

    method, props, body = channel.basic_get("test_check_queue", auto_ack=True)
    if body:
        data = json.loads(body)
        print(f"Received: {data}")
        assert data == msg
        print("AMQP round-trip OK")
    else:
        print("No message returned!")

    channel.queue_delete(queue="test_check_queue")
    connection.close()


def check_http_api():
    print(f"\n🌐 Checking RabbitMQ HTTP API at http://{RABBIT_HOST}:{HTTP_PORT}/api/...")
    base_url = f"http://{RABBIT_HOST}:{HTTP_PORT}/api"
    auth = HTTPBasicAuth(USER, PASS)

    # Check overview
    resp = requests.get(f"{base_url}/overview", auth=auth, timeout=5)
    resp.raise_for_status()
    print("HTTP API reachable")

    # Check that your exchange and queue exist
    exchanges = requests.get(f"{base_url}/exchanges", auth=auth, timeout=5).json()
    queues = requests.get(f"{base_url}/queues", auth=auth, timeout=5).json()

    found_exchange = any(e["name"] == "pollsExchange" for e in exchanges)
    found_queue = any(q["name"] == "pollApp.votes" for q in queues)

    if found_exchange:
        print("Found exchange: pollsExchange")
    else:
        print("pollsExchange not found")

    if found_queue:
        print("Found queue: pollApp.votes")
    else:
        print("pollApp.votes not found")

    if found_exchange and found_queue:
        print("\n RabbitMQ fully configured and operational!")
    else:
        print("\n RabbitMQ running but missing expected exchange/queue.")


if __name__ == "__main__":
    try:
        check_amqp_connection()
        check_http_api()
    except Exception as e:
        print(f"\n RabbitMQ check failed: {e}")
        sys.exit(1)
