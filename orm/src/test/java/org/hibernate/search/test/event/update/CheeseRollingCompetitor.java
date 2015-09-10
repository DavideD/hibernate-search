/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.event.update;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Indexed
class CheeseRollingCompetitor {

	@DocumentId
	private Integer id;

	@Field(name = "Nickname", index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String nickname;

	public CheeseRollingCompetitor() {
	}

	public CheeseRollingCompetitor(Integer id, String name) {
		this.id = id;
		this.nickname = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "CheeseRollingCompetitor [id=" );
		builder.append( id );
		builder.append( ", nickname=" );
		builder.append( nickname );
		builder.append( "]" );
		return builder.toString();
	}
}