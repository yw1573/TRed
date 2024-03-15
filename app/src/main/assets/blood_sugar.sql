
DROP TABLE BloodSugars;
CREATE TABLE [BloodSugars] ( 
    [id]          INTEGER PRIMARY KEY,
    [timestamp]   INTEGER UNIQUE,
    [phase]       TEXT,
    [value]       REAL 
);

-- CREATE INDEX idx_bloodsugars_timestamp ON BloodSugars(timestamp);

DROP TABLE PhaseString;
CREATE TABLE [PhaseString] ( 
    [id]     INTEGER PRIMARY KEY AUTOINCREMENT,
    [phase]  CHAR    UNIQUE,
    [desc] 	 CHAR 
);

INSERT INTO PhaseString (phase) VALUES ('空腹');
INSERT INTO PhaseString (phase) VALUES ('餐前');
INSERT INTO PhaseString (phase) VALUES ('餐后');
INSERT INTO PhaseString (phase) VALUES ('睡前');
INSERT INTO PhaseString (phase) VALUES ('随机');
