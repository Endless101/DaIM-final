-- E1 query
select publisher
from proceedings p inner join conference c on p.proceedings_key = c.proceedings_key
where c.title = "' PODS '" LIMIT 1;
-- Result:
-- Publisher = ACM

-- E2 query
select ar.title
from article ar
join author a on ar.article_key = a.publication_key and  a.author = "'Martin Grohe'"
join journal j on ar.article_key = j.article_key and j.title = "' Theor. Comput. Sci. '"
order by ar.title;
-- Result:
-- The titles of the articles are:
-- Machine-based methods in parameterized complexity theory.
-- The complexity of partition functions.
-- Tight lower bounds for query processing on streaming and external memory data.

-- M1 query
select count(distinct inproceeding_key)
from inproceeding
inner join conference
on inproceeding_key = conference.proceedings_key
where inproceeding.year = "'2022'" and conference.title = "' SIGMOD Conference '";
-- Result:
-- Number of articles = 222

-- M2 query
select count(*) as Amount, j.title
from article a inner join journal j on a.article_key = j.article_key
where Year = ( select min(Year) from article)
group by Journal;
-- Result:
-- The oldest journal is = J. Symb. Log.
-- Number of articles = 12

-- M3 query
select distinct percentile_cont(0.5)
within group (order by NumberOfArticles) over () as median
from (select count(inproceeding_key) as NumberOfArticles
    from inproceeding i
    inner join conference c on i.inproceeding_key = c.proceedings_key
    where c.title = "' CIDR '"
    group by i.year) t;
-- Result:
-- The median amount of articles = 44.5

-- M4 query
select i.year
from author a
inner join conference c on c.proceedings_key = a.publication_key
inner join inproceeding i on i.inproceeding_key = a.publication_key
where c.title = "' SIGMOD Conference '"
group by c.title, i.year
having count(a.author) >10
order by count(a.author) desc limit 1;
-- Result:
-- The year = 2021

-- M5 query
select editor, amount from
(select e.editor as editor, count(*) as amount
    from editor e
    inner join conference c on e.proceedings_key = c.proceedings_key
    where c.title = "' PODS '"
    group by e.editor) t
where amount > 2
order by amount desc;
-- Result:
-- The most frequent editors are:
-- Leonid Libkin, an editor 3 times
-- Maurizio Lenzerini, an editor 3 times

-- M6 query
select author,count(distinct conference.title)
from (select *
    from author
    where author.author =
    (select author
        from(select author,max(publishcount)
            from (select author, count(author) as publishcount
                from author
                group by author
                order by count(author.author) desc) as maxAuthorPublishCount) as maxAuthor)) as maxAuthorAndKeyTable
inner join conference
on maxAuthorAndKeyTable.publication_key = conference.proceedings_key;
-- Result:
-- The researcher with the most overall = Jos
-- Number of conferences they published to = 4060

-- H1 query
CREATE TABLE new_authors
SELECT *
FROM
--Table that contains all the authors and their co-authors
(SELECT a3.author as author, a3.publication_key as author_key
FROM author a3
WHERE a3.publication_key = ANY
--Table that contains all the conference and journal keys that could hold co-authors
(SELECT a2.publication_key
FROM author a2
WHERE a2.author = ANY
--Table that contains all those specific authors
(SELECT DISTINCT a.author
FROM conference c
INNER JOIN author a ON c.proceedings_key = a.publication_key
INNER JOIN inproceeding i ON c.proceedings_key = i.inproceeding_key
WHERE c.title = "' ICDT '" AND i.`year` = "'2020'")))

--We define a second query as we need the above table to computer the inner product
SELECT t1.author, t1.coauthor, MAX(t1.timesWorkedTogether)
    (SELECT n1.author AS author, n2.author AS coauthor, COUNT(n1.author_key) AS timesWorkedTogether
    FROM new_authors n1, new_authors n2
    WHERE n1.author_key = n2.author_key) t1
    GROUP BY t1.author, t1.coauthor
--We did not at the result for this query as it took too long to run