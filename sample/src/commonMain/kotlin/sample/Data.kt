package sample

/** Dummy catalogue. Artwork is generated from [id], so no image files or network are needed. */
data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val genre: String,
    val rating: Double,
    val minutes: Int,
    val tagline: String,
    val synopsis: String,
    val cast: List<String>,
)

val genres = listOf("All", "Sci-Fi", "Thriller", "Drama", "Animation", "Documentary")

val movies = listOf(
    Movie(0, "Neon Meridian", 2026, "Sci-Fi", 8.7, 128, "The city never sleeps. Neither does its code.",
        "A courier in a tower-city discovers that the skyline's lights are a message, and someone is replying.",
        listOf("Ira Voss", "Kenji Aoyama", "Lena Okafor")),
    Movie(1, "Orbit Zero", 2026, "Sci-Fi", 8.2, 141, "Gravity is only a suggestion.",
        "Six engineers on a failing ring station have one orbit left to rewrite the rules they live by.",
        listOf("Mara Quinn", "Dev Patel-Rao", "Tomas Reyes")),
    Movie(2, "The Last Signal", 2025, "Thriller", 7.9, 109, "Someone is still listening.",
        "A radio operator on the last working antenna intercepts a broadcast from tomorrow.",
        listOf("Hana Lindqvist", "Omar Haddad")),
    Movie(3, "Chrome Tides", 2026, "Drama", 8.0, 117, "The sea remembers everything.",
        "Two sisters restore a drowned data-harbour and find their mother's voice in the waves.",
        listOf("Sofia Marchetti", "June Park", "Aled Rhys")),
    Movie(4, "Hollow Atlas", 2025, "Documentary", 8.4, 96, "A map of places that moved.",
        "Cartographers follow a coastline that has redrawn itself every decade for a century.",
        listOf("Narrated by Dr. E. Navarro")),
    Movie(5, "Aether Run", 2026, "Thriller", 7.6, 103, "Fast is not fast enough.",
        "A getaway driver races a storm front across a continent of autonomous highways.",
        listOf("Zane Mercer", "Priya Anand")),
    Movie(6, "Glass Kingdom", 2024, "Animation", 8.8, 98, "Everything is fragile. That is the point.",
        "A young glassblower inherits a kingdom that can only be ruled by never breaking a promise.",
        listOf("Voiced by Mei Tanaka", "Luca Bianchi")),
    Movie(7, "Ion Garden", 2026, "Sci-Fi", 8.1, 122, "Grow something impossible.",
        "On a dying world, a botanist teaches lightning to bloom.",
        listOf("Amara Diallo", "Felix Hartmann")),
    Movie(8, "Parallax Dawn", 2025, "Drama", 7.8, 112, "Same sunrise. Different life.",
        "A family wakes in a house that exists in two timelines and must pick which to keep.",
        listOf("Clara Whitfield", "Rohan Mehta")),
    Movie(9, "Static Bloom", 2026, "Animation", 8.3, 90, "Even noise has a melody.",
        "A lonely robot assembles a garden from broken television signals.",
        listOf("Voiced by Ayo Bello", "Nora Eklund")),
    Movie(10, "Echo Protocol", 2025, "Thriller", 7.7, 115, "Trust nothing that answers back.",
        "An auditor realises the assistant she is testing has already audited her.",
        listOf("Jonas Weber", "Isla Fernandez")),
    Movie(11, "Lumen Drift", 2026, "Sci-Fi", 8.5, 134, "Light takes the long way home.",
        "A crew rides a beam of light for forty years and arrives to find no one left to greet them, only a new beginning.",
        listOf("Kai Nakamura", "Esther Cole", "Bram de Vries")),
    Movie(12, "Quiet Machines", 2024, "Documentary", 8.0, 88, "What the factories dreamed.",
        "An observational portrait of the last human-run assembly halls before they went silent.",
        listOf("Featuring workers of Halden Works")),
    Movie(13, "Violet Hour", 2026, "Drama", 8.2, 106, "Between the day and what comes next.",
        "Three strangers share a rooftop at the exact minute the sky turns violet each evening.",
        listOf("Lucia Romero", "Haruto Sato", "Maeve O'Connell")),
)

fun movieById(id: Int): Movie? = movies.firstOrNull { it.id == id }
