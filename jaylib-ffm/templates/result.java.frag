com.raylib.jextract.raylib_h.{{ function.name }}({% if function.needs_allocator %}Arena.ofAuto(){% if function.params %}, {% endif %}{% endif %}
            {% for param in function.params %}{{ param.converter_to_c_type }}{{ ", " if not loop.last else "" }}{% endfor %}
        ){{ function.converter_from_memorysegment }}