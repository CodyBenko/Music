CREATE TABLE "artist"(
    "ArtistId" INT PRIMARY KEY NOT NULL,
    "Name" VARCHAR
);

CREATE TABLE "album"(
    "AlbumId" INT NOT NULL,
    "Title" VARCHAR NOT NULL,
    "ArtistId" INT NOT NULL,
    CONSTRAINT "PK_Album" PRIMARY KEY("AlbumId")
    CONSTRAINT "FK_AlbumArtistId" FOREIGN KEY ("ArtistId") REFERENCES "artist" ("ArtistId") ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE INDEX "IFK_AlbumArtistId" ON "album" ("ArtistId");

INSERT INTO "artist" VALUES (1, 'Ghost');
INSERT INTO "artist" VALUES (2, 'DragonForce');