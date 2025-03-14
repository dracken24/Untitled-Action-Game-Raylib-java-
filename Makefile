JAVAC = javac
JAVA = java
BIN_DIR = bin

CP = jaylib-ffm-5.5.0-2.jar

JAVA_FILES = src/App.java \
            src/com/game/Core.java \
            src/com/objects/Platform.java \
            src/com/objects/SpriteSheet.java \
            src/com/player/Player.java \
            src/com/player/InitPlayer.java \
            src/com/player/PlayerMovement.java \
            src/com/enums/PlayerType.java \
            src/com/enums/SpriteMovement.java \
            src/com/physic/PhysicCore.java \
            src/com/physic/Gravity.java \
            src/com/game/Cameras.java \
            src/com/objects/MovableObject.java \
            src/com/interfaces/IMovable.java \
            src/com/physic/Collision.java \
            src/com/enemy/Enemy.java \
            src/com/enemy/EnemyMovement.java \
            src/com/enemy/InitEnemy.java

all: compile

compile:
	@if not exist $(BIN_DIR) mkdir $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) -cp $(CP) $(JAVA_FILES)

run: compile
	$(JAVA) --enable-native-access=ALL-UNNAMED -cp $(CP);$(BIN_DIR) App

clean:
	@if exist $(BIN_DIR) rd /s /q $(BIN_DIR) 

.PHONY: all compile run clean