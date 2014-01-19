package com.cesspoollife.rgrong;

import org.jsoup.nodes.Document;

public interface AsyncResponse {
	void processFinish(Document doc);
	void contentsFinish(Document doc);
	void loginFinish(Document doc);
}
