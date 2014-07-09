.PHONY: clean update check date diff

check:clean
	git add -A
	git status
date:clean
	git pull origin master
update:clean
	git add -A
	git status
	git commit -m "update"
	git push origin master
diff:
	git add -A
	git diff --cached
clean:
	find . -name \*\.e -type f -delete 
	find . -name \*\.o -type f -delete 
	find . -name \*~ -type f -delete 
