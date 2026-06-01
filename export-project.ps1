$OutputFile = "project_dump.txt"

Remove-Item $OutputFile -ErrorAction Ignore

$ExcludePattern = "node_modules|target|dist|build|\.git|\.idea|\.vscode|coverage|out"

$AllowedPatterns = @(
    "\.java$",
    "\.kt$",
    "\.groovy$",
    "\.xml$",
    "\.yml$",
    "\.yaml$",
    "\.properties$",
    "\.json$",
    "\.sql$",
    "\.js$",
    "\.jsx$",
    "\.ts$",
    "\.tsx$",
    "\.html$",
    "\.css$",
    "\.scss$",
    "\.md$",
    "\.txt$",
    "\.sh$",
    "\.bat$",
    "\.ps1$",
    "Dockerfile",
    "docker-compose",
    "\.env",
    "\.gitignore",
    "pom\.xml",
    "package\.json",
    "package-lock\.json",
    "vite\.config",
    "nginx\.conf"
)

$Files = Get-ChildItem -Recurse -File | Where-Object {

    $Path = $_.FullName

    if ($Path -match $ExcludePattern) {
        return $false
    }

    foreach ($Pattern in $AllowedPatterns) {
        if ($Path -match $Pattern) {
            return $true
        }
    }

    return $false
}

foreach ($File in $Files) {

    $RelativePath = Resolve-Path -Relative $File.FullName

    @"

==================================================
FILE: $RelativePath
==================================================

"@ | Out-File $OutputFile -Append -Encoding utf8

    try {
        Get-Content $File.FullName -Raw -Encoding UTF8 |
        Out-File $OutputFile -Append -Encoding utf8
    }
    catch {
        "[ERROR READING FILE]" |
        Out-File $OutputFile -Append -Encoding utf8
    }

    "`n`n" | Out-File $OutputFile -Append -Encoding utf8
}

Write-Host "DONE -> $OutputFile"