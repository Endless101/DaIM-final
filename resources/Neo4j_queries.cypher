// These Cypher queries give the same results as MariaDB queries

// E1 query
match(pro:proceeding)-[:APPEARED_IN]->(conf:conference {name: "' PODS '"})
return pro.publisher LIMIT 1;

// E2 query
match(auth:author {name: "'Martin Grohe'"})-[:AUTHORED]->(a:article)-[:APPEARED_IN]->(j:journal {name: "' Theor. Comput. Sci. '"})
return a.title;

// M1 query
match(:inproceeding {year: "'2022'"})-[r:APPEARED_IN]->(:conference {name: "' SIGMOD Conference '"})
return count(r);

// M2 query
match(n:article) with min(n.year) as v
match(s:article {year: v})-[:APPEARED_IN]->(j)
return count(*),j.name limit 1;

// M3 query
CALL {MATCH(inp:inproceeding)-[APPEARED_IN]->(conf:conference {name: "' CIDR '"})
	RETURN inp.year AS year, COUNT(*) AS NumberOfArticles
	ORDER BY NumberOfArticles}
RETURN percentileDisc(NumberOfArticles, 0.5);

// M4 query
match(:author)-[a:AUTHORED]->(inp:inproceeding)-[:APPEARED_IN]->(:conference {name: "' SIGMOD Conference '"})
with count(a) as amountOfAuthors, inp.year as year
where amountOfAuthors > 10
return year order by amountOfAuthors desc limit 1;

// M5 query
match(n:editor)-[e:EDITED]->(:proceeding)-[*]->(:conference {name: "' PODS '"})
with n, count(e) as amountEdited order by amountEdited desc limit 1
match(a:editor)-[v:EDITED]->(:proceeding)-[*]->(:conference {name: "' PODS '"})
with a as a, count(v) as amount, max(amountEdited) as maxx
where amount = maxx
return a.name, amount;

// M6 query
match(n:author)-[r]->(pub)
where pub:article or pub:inproceeding
with n,count(r) as publications order by publications desc limit 1
match(n:author)--()-[rs]->(c:conference)
return n, count(distinct c);

// H1

match(n:author)-->(ar {year: "'2020'"})-->(c:conference {name: " 'ICDT' "})
match(n)-->(ar2)
match(co:author)-->(ar2)
	where not n = co
with n,collect(co) as coAuthor
call {
with n, coAuthor
unwind coAuthor as cas
with n,cas,count(cas) as ccas
with n, max(ccas) as maxes
return n as someNode,maxes as maxes
}
call{
with n,coAuthor, maxes
unwind coAuthor as cas
with maxes,n,cas,count(cas) as ccas
	where ccas=maxes
return n as finalAuthor, ccas as countCoAuthors, cas as coAuthors
} return finalAuthor,countCoAuthors,coAuthors;