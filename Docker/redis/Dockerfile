FROM redis:latest

ENV REDIS_PASSWORD=root

CMD ["redis-server", "--requirepass", "root"]


# Run from the root of repo

# docker build --no-cache -f Docker/redis/Dockerfile -t  walgreens/redis .
# docker run -p 6379:6379 -it walgreens/redis