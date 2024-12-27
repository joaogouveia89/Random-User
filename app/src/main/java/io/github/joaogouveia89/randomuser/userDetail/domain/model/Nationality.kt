package io.github.joaogouveia89.randomuser.userDetail.domain.model

enum class Nationality(
    val reference: String,
    val displayName: String
) {
    AU("AU", "Australian"),
    BR("BR", "Brazilian"),
    CA("CA", "Canadian"),
    CH("CH", "Swiss"),
    DE("DE", "German"),
    DK("DK", "Danish"),
    ES("ES", "Spanish"),
    FI("FI", "Finnish"),
    FR("FR", "French"),
    GB("GB", "British"),
    IE("IE", "Irish"),
    IN("IN", "Indian"),
    IR("IR", "Iranian"),
    MX("MX", "Mexican"),
    NL("NL", "Dutch"),
    NO("NO", "Norwegian"),
    NZ("NZ", "New Zealander"),
    RS("RS", "Serbian"),
    TR("TR", "Turkish"),
    UA("UA", "Ukrainian"),
    US("US", "American");

    companion object {
        fun fromReference(ref: String): Nationality? {
            return entries.find { it.reference == ref }
        }
    }
}