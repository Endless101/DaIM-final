 CREATE INDEX article IF NOT EXISTS for (a:article) on a.key;
 CREATE INDEX proceeding IF NOT EXISTS  for (p:proceeding) on p.key;
 CREATE INDEX inproceeding IF NOT EXISTS   for (ip:inproceeding) on ip.key;
 CREATE INDEX author IF NOT EXISTS  for (a:author) on a.name;
 CREATE INDEX conference IF NOT EXISTS  for (c:conference) on c.name;
 CREATE INDEX journal IF NOT EXISTS  for (j:journal) on j.name;
 CREATE INDEX editor IF NOT EXISTS  for (e:editor) on e.name;
 CALL db.awaitIndexes();



 LOAD CSV WITH HEADERS FROM 'file:///article.csv' as row
 CALL {
 with row
     CREATE(a:article {
     volume: row.volume,
     number: row.number,
     pages: row.pages,
     journal: row.journal,
     year: row.year,
     publisher: row.publisher,
     title: row.title,
     key: row.key
             })
 } IN TRANSACTIONS OF 500 ROWS;


LOAD CSV WITH HEADERS FROM 'file:///proceedings.csv' as row
CALL {
with row
    CREATE(p:proceeding {
    volume: row.volume,
    year: row.year,
    publisher: row.publisher,
    booktitle: row.booktitle,
    title: row.title,
    key: row.key
            })
} IN TRANSACTIONS OF 500 ROWS;


LOAD CSV WITH HEADERS FROM 'file:///inproceeding.csv' as row
CALL {
with row
    CREATE(ip:inproceeding {
    year: row.year,
    pages: row.pages,
    booktitle: row.booktitle,
    title: row.title,
    key: row.key
            })
} IN TRANSACTIONS OF 500 ROWS;



 LOAD CSV WITH HEADERS FROM 'file:///journal.csv' as row
 CALL {
 with row
     merge(j:journal {
     name: row.journal
             })
 } IN TRANSACTIONS OF 500 ROWS;


 LOAD CSV WITH HEADERS FROM 'file:///conference.csv' as row
 CALL {
 with row
     merge(c:conference {
     name: row.conference
             })
 } IN TRANSACTIONS OF 500 ROWS;



 LOAD CSV WITH HEADERS FROM 'file:///editor.csv' as row
 CALL {
  with row
     merge(e:editor {
     name: row.editor
     })
 } IN TRANSACTIONS OF 500 ROWS;


LOAD CSV WITH HEADERS FROM 'file:///author.csv' as row
 CALL {
 with row
     MERGE(j:author {
     name: row.author
             })
 } IN TRANSACTIONS OF 500 ROWS;


// RELATIONSHIPS
LOAD CSV WITH HEADERS FROM 'file:///editor.csv' as row
 CALL {
  with row
     match(proc:proceeding {key: row.key}) match(editor:editor {name: row.editor})
     create(editor)-[:EDITED]->(proc)
     } IN TRANSACTIONS OF 500 ROWS;

LOAD CSV WITH HEADERS FROM 'file:///author.csv' as row
 CALL {
  with row
     match(n:article {key: row.key})  
     match(au:author {name: row.author})
     WHERE n:article OR n:inproceeding 
     create(au)-[:AUTHORED]->(n)
     } IN TRANSACTIONS OF 500 ROWS;



LOAD CSV WITH HEADERS FROM 'file:///author.csv' as row
 CALL {
  with row
     match(n:inproceeding {key: row.key})  
     match(au:author {name: row.author})
     WHERE n:article OR n:inproceeding 
     create(au)-[:AUTHORED]->(n)
     } IN TRANSACTIONS OF 500 ROWS;


LOAD CSV WITH HEADERS FROM 'file:///journal.csv' as row
 CALL {
  with row
     match(article:article {key: row.key}) match(jour:journal {name: row.journal})
     create(article)-[:APPEARED_IN]->(jour)
     } IN TRANSACTIONS OF 500 ROWS;

LOAD CSV WITH HEADERS FROM 'file:///conference.csv' as row
 CALL {
  with row
     match(proc {key: row.key}) match(conf:conference {name: row.conference})
     WHERE proc:inproceeding OR proc:proceeding
     create(proc)-[:APPEARED_IN]->(conf)
     } IN TRANSACTIONS OF 500 ROWS;


