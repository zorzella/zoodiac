import os
from pathlib import Path

def list_files(start_path="."):
    html = ['<html><head><title>Index of Update Site</title></head><body>']
    html.append("<h1>Index of Update Site</h1>")
    html.append("<ul>")

    for root, dirs, files in os.walk(start_path):
        rel_root = os.path.relpath(root, start_path)
        for f in sorted(files):
            path = os.path.join(rel_root, f) if rel_root != '.' else f
            url = path.replace("\\", "/")
            html.append(f'<li><a href="{url}">{url}</a></li>')

    html.append("</ul></body></html>")
    return "\n".join(html)

output = list_files(".")
with open("index.html", "w", encoding="utf-8") as f:
    f.write(output)

print("index.html generated.")
