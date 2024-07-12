package tn.esprit.version_checker.network

data class VersionInfo(
    val version: String,
    val mandatory: Boolean,
    val createdAt: String // Adjust data type as per your MongoDB schema
)
