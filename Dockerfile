FROM ubuntu:latest
LABEL authors="Nobody"

ENTRYPOINT ["top", "-b"]