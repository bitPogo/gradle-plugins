/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.signing

import com.appmattus.kotlinfixture.kotlinFixture
import groovy.lang.Closure
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import java.util.SortedMap
import java.util.SortedSet
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.DomainObjectCollection
import org.gradle.api.NamedDomainObjectCollectionSchema
import org.gradle.api.Namer
import org.gradle.api.Project
import org.gradle.api.Rule
import org.gradle.api.Task
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.plugins.signing.Sign
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingApiContract.MemorySigning
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.api.MemorySigningConfiguration
import tech.antibytes.gradle.publishing.publisher.TestConfig
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeMapProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeSetProperty
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.versioning.VersioningContract

@Suppress("UNCHECKED_CAST")
class SigningControllerSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkObject(
            MemorySignature,
            CommonSignature,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            MemorySignature,
            CommonSignature,
        )
    }

    @Test
    fun `It fulfils SigningController`() {
        val controller: Any = SigningController

        assertTrue(controller is PublishingContract.SigningController)
    }

    @Test
    fun `Given configure is called without signing configuration, it does not call MemorySigning`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(MemorySigning::class.java, null),
        )

        val project: Project = mockk()
        val root: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(any()) }
        verify(exactly = 0) { MemorySignature.configure(any(), any()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration, it calls MemorySigning`() {
        // Given
        val name: String = fixture()
        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val signingTasks: MutableList<Sign> = mutableListOf(
            mockk(),
            mockk(),
            mockk(),
        )
        val publicationTaskConfiguration = slot<Action<AbstractPublishToMaven>>()
        val taskContainer: TaskContainer = mockk {
            every { withType(AbstractPublishToMaven::class.java, capture(publicationTaskConfiguration)) } returns mockk()
            every { withType(Sign::class.java) } returns FakeDomainObjectCollection(signingTasks)
        }
        val project: Project = mockk {
            every { tasks } returns taskContainer
        }
        val root: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 1) { CommonSignature.configure(project) }
        verify(exactly = 1) { MemorySignature.configure(project, config.signing.get()) }
        val samplePublishingTask: AbstractPublishToMaven = mockk(relaxed = true)
        publicationTaskConfiguration.captured.execute(samplePublishingTask)

        verify(exactly = 1) {
            samplePublishingTask.mustRunAfter(*signingTasks.toTypedArray())
        }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if it is excluded by the root`() {
        // Given
        val name: String = fixture()
        val subName: String = fixture()
        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(subName)),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns subName

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if they have no PublishingExtension`() {
        // Given
        val name: String = fixture()
        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns name

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns null

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if it is a standalone`() {
        // Given
        val name: String = fixture()
        val subprojectExtension: PublishingPluginExtension = createExtension()
        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns name

        subprojectExtension.standalone.set(true)

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns subprojectExtension

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if it has a signing Configuration`() {
        // Given
        val name: String = fixture()
        val subprojectExtension: PublishingPluginExtension = createExtension()
        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns name

        subprojectExtension.standalone.set(false)
        subprojectExtension.signing.set(mockk<MemorySigning>())

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns subprojectExtension

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if it is excluded`() {
        // Given
        val name: String = fixture()
        val subName: String = fixture()
        val subprojectExtension: PublishingPluginExtension = createExtension()
        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns subName

        subprojectExtension.standalone.set(false)
        subprojectExtension.signing.set(null)
        subprojectExtension.excludeProjects.set(setOf(subName))

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns subprojectExtension

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it configures the subprojects`() {
        // Given
        val name: String = fixture()
        val subName: String = fixture()
        val subprojectExtension: PublishingPluginExtension = createExtension()
        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val signingTasks: MutableList<Sign> = mutableListOf(
            mockk(),
            mockk(),
            mockk(),
        )
        val publicationTaskConfiguration = slot<Action<AbstractPublishToMaven>>()
        val taskContainer: TaskContainer = mockk {
            every { withType(AbstractPublishToMaven::class.java, capture(publicationTaskConfiguration)) } returns mockk()
            every { withType(Sign::class.java) } returns FakeDomainObjectCollection(signingTasks)
        }
        val project: Project = mockk()
        val subProject: Project = mockk {
            every { tasks } returns taskContainer
        }

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns subName

        subprojectExtension.standalone.set(false)
        subprojectExtension.signing.set(null)
        subprojectExtension.excludeProjects.set(emptySet())

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns subprojectExtension

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 1) { CommonSignature.configure(subProject) }
        verify(exactly = 1) { MemorySignature.configure(subProject, config.signing.get()) }
        val samplePublishingTask: AbstractPublishToMaven = mockk(relaxed = true)
        publicationTaskConfiguration.captured.execute(samplePublishingTask)

        verify(exactly = 1) {
            samplePublishingTask.mustRunAfter(*signingTasks.toTypedArray())
        }
    }
}

private class FakeDomainObjectCollection<T : Task>(
    private val collection: MutableCollection<T>,
) : TaskCollection<T> {
    override fun contains(element: T?): Boolean = collection.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = collection.containsAll(elements)
    override fun isEmpty(): Boolean = collection.isEmpty()
    override fun add(element: T): Boolean = collection.add(element)
    override fun addAll(elements: Collection<T>): Boolean = collection.addAll(elements)
    override fun clear() = collection.clear()
    override fun iterator(): MutableIterator<T> = collection.toMutableList().listIterator()
    override fun remove(element: T): Boolean = collection.remove(element)

    override fun removeAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun addLater(provider: Provider<out T>) {
        TODO("Not yet implemented")
    }

    override fun addAllLater(provider: Provider<out MutableIterable<T>>) {
        TODO("Not yet implemented")
    }

    override fun <S : T?> withType(type: Class<S>): TaskCollection<S> {
        TODO("Not yet implemented")
    }

    override fun <S : T?> withType(
        type: Class<S>,
        configureAction: Action<in S>,
    ): DomainObjectCollection<S> {
        TODO("Not yet implemented")
    }

    override fun <S : T?> withType(
        type: Class<S>,
        configureClosure: Closure<*>,
    ): DomainObjectCollection<S> {
        TODO("Not yet implemented")
    }

    override fun matching(spec: Spec<in T>): TaskCollection<T> {
        TODO("Not yet implemented")
    }

    override fun matching(spec: Closure<*>): TaskCollection<T> {
        TODO("Not yet implemented")
    }

    override fun whenObjectAdded(action: Action<in T>): Action<in T> {
        TODO("Not yet implemented")
    }

    override fun whenObjectAdded(action: Closure<*>) {
        TODO("Not yet implemented")
    }

    override fun whenObjectRemoved(action: Action<in T>): Action<in T> {
        TODO("Not yet implemented")
    }

    override fun whenObjectRemoved(action: Closure<*>) {
        TODO("Not yet implemented")
    }

    override fun all(action: Action<in T>) {
        TODO("Not yet implemented")
    }

    override fun all(action: Closure<*>) {
        TODO("Not yet implemented")
    }

    override fun configureEach(action: Action<in T>) {
        TODO("Not yet implemented")
    }

    override fun findAll(spec: Closure<*>): TaskCollection<T> {
        TODO("Not yet implemented")
    }

    override fun getNamer(): Namer<T> {
        TODO("Not yet implemented")
    }

    override fun getAsMap(): SortedMap<String, T> {
        TODO("Not yet implemented")
    }

    override fun getNames(): SortedSet<String> {
        TODO("Not yet implemented")
    }

    override fun findByName(name: String): T? {
        TODO("Not yet implemented")
    }

    override fun getByName(name: String, configureClosure: Closure<*>): T {
        TODO("Not yet implemented")
    }

    override fun getByName(name: String): T {
        TODO("Not yet implemented")
    }

    override fun getAt(name: String): T {
        TODO("Not yet implemented")
    }

    override fun addRule(rule: Rule): Rule {
        TODO("Not yet implemented")
    }

    override fun addRule(description: String, ruleAction: Closure<*>): Rule {
        TODO("Not yet implemented")
    }

    override fun addRule(description: String, ruleAction: Action<String>): Rule {
        TODO("Not yet implemented")
    }

    override fun getRules(): MutableList<Rule> {
        TODO("Not yet implemented")
    }

    override fun named(nameFilter: Spec<String>): TaskCollection<T> {
        TODO("Not yet implemented")
    }

    override fun named(name: String): TaskProvider<T> {
        TODO("Not yet implemented")
    }

    override fun getCollectionSchema(): NamedDomainObjectCollectionSchema {
        TODO("Not yet implemented")
    }

    override fun whenTaskAdded(closure: Closure<*>) {
        TODO("Not yet implemented")
    }

    override fun whenTaskAdded(action: Action<in T>): Action<in T> {
        TODO("Not yet implemented")
    }

    override fun <S : T> named(name: String, type: Class<S>, configurationAction: Action<in S>): TaskProvider<S> {
        TODO("Not yet implemented")
    }

    override fun <S : T> named(name: String, type: Class<S>): TaskProvider<S> {
        TODO("Not yet implemented")
    }

    override fun named(name: String, configurationAction: Action<in T>): TaskProvider<T> {
        TODO("Not yet implemented")
    }

    override fun getByName(name: String, configureAction: Action<in T>): T {
        TODO("Not yet implemented")
    }

    override val size: Int = collection.size
}
