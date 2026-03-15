.PHONY: build start up stop down restart logs ps urls

build:
	./scripts/build-images.sh

start:
	./scripts/start-stack.sh

up: start

stop:
	docker compose stop

down:
	docker compose down

restart:
	docker compose down
	./scripts/start-stack.sh

logs:
	docker compose logs -f

ps:
	docker compose ps

urls:
	./scripts/show-app-urls.sh
