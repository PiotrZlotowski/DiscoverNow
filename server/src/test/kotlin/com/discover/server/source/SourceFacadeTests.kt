package com.discover.server.source

import com.discover.server.authentication.User
import com.discover.server.authentication.UserAlreadySubscribedException
import com.discover.server.feed.FeedService
import com.discover.server.feed.RssFeedService
import com.discover.server.search.SearchService
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import ma.glasnost.orika.MapperFacade
import org.assertj.core.api.Assertions.catchThrowable
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SourceFacadeTests {

    @InjectMockKs
    private lateinit var sut: SourceFacade

    @MockK
    private lateinit var mapperFacade: MapperFacade
    @MockK
    private lateinit var searchService: SearchService<Source>
    @MockK
    private lateinit var rssFeedService: RssFeedService
    @MockK
    private lateinit var feedService: FeedService
    @MockK
    private lateinit var sourceService: SourceService

    @Test
    fun `subscribeToSource should subscribe a given user to the source whenever requested`() {
        // GIVEN
        val user = createUser("test1@xyz.com")
        val alreadySubscribedUser = createUser("test2@xyz.com")
        val source = Source(users = mutableListOf(alreadySubscribedUser))

        every { sourceService.getSource("1") } returns source
        every { sourceService.saveSource(source) } returns source
        every { sourceService.isUserAlreadySubscribedToSource(source, user) } returns false


        // WHEN
        sut.subscribeToSource("1", user)

        verify { sourceService.saveSource(source) }
    }

    @Test
    fun `unsubscribeSource should remove subscription and then remove all feeds whenever requested user was the only subscriber of that source`() {
        // GIVEN
        val user = createUser("test1@xyz.com")
        val source = Source(users = mutableListOf(user))
        every { sourceService.getSource("1") } returns source
        every { feedService.deleteAllFeeds(source.feeds) } just Runs
        every { sourceService.saveSource(source) } returns source
        every { sourceService.isUserAlreadySubscribedToSource(source, user) } returns false

        // WHEN
        sut.unSubscribeSource("1", user)

        // THEN
        verify { feedService.deleteAllFeeds(source.feeds) }

    }


    @Test
    fun `subscribeToSource should not allow to subscribe a source if user is already subscribed to it`() {
        val user = createUser("test1@xyz.com")
        val source = Source(users = mutableListOf(user))

        every { sourceService.getSource("1") } returns source
        every { sourceService.saveSource(source) } returns source
        every { sourceService.isUserAlreadySubscribedToSource(source, user) } returns true

        // WHEN
        val actualException = catchThrowable { sut.subscribeToSource("1", user) }

        verify(exactly = 0) { sourceService.saveSource(source) }
        every { sourceService.isUserAlreadySubscribedToSource(source, user) }
        then(actualException).isExactlyInstanceOf(UserAlreadySubscribedException::class.java)


    }

    private fun createUser(email: String)
            = User(email = email, password = "pwd1", roles = emptySet(), compilations = emptySet(), sources = emptyList())

}
