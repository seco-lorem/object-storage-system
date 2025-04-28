package minio.authz

default allow = false

allow if {
    input.user == "admin"
    input.action in valid_action
    validate[input.action]
}

valid_action := {
    "create_bucket",
    "upload",
    "download"
}

validate["create_bucket"] if {
    startswith(input.bucket, team_name)
    input.bucket_versioning == true
    n := input.bucket_size_bytes
    n <= max_bucket_size
}

validate["upload"] if {
    input.bucket != ""
    startswith(input.bucket, team_name)
}

validate["download"] if {
    input.bucket != ""
    startswith(input.bucket, team_name)
}

team_name := "teamname"
max_bucket_size := 1073741824
