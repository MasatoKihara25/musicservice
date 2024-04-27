package com.example.musicservice;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * ミュージック情報に関して HTTPリクエストを受け付けるコントローラ
 */
@RestController
public class MusicController {

    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    //    @GetMapping("/music")
//    public List<Music> getMusic() {
//        List<Music> music = musicMapper.findAll();
//        return music;
//    }
//    @GetMapping("/music")
//    public List<Music> findByMusic(MusicSearchRequest request) {
//        List<Music> music = musicMapper.findMusic(request.getStartsWith(), request.getEndsWith());
//        return music;
//    }


    //    @GetMapping("/music")
//    public List<Music> findByMusic(@RequestParam String startsWith) {
//        List<Music> music = musicService.findMusicStartingWith(startsWith);
//        return music;
//    }

    /**
     * GETリクエストで全件出力またはクエリ文字列による検索
     *
     * @param musicSearchRequest
     * @return
     */
    @GetMapping("/music")
    public List<Music> findByMusic(MusicSearchRequest musicSearchRequest) {
        List<Music> music = musicService.findMusicStartingWith(musicSearchRequest.getStartsWith());
        return music;
    }

    @GetMapping("/music/{id}")
    public Music findMusic(@PathVariable("id") Integer id) {
        return musicService.findMusic(id);
    }

    /**
     * ステータスコード200で返すバージョン
     *
     * @param request
     * @return
     */
//    @PostMapping("/music")
//    public Music insert(@RequestBody MusicRequest request) {
//        Music music = musicService.insert(request.getTitle(), request.getArtist());
//        return music;
//    }

    /**
     * ステータスコード201でレスポンスボディを返す
     * ロケーションヘッダーを返す
     *
     * @param musicRequest
     * @param uriBuilder
     * @return
     */
    @PostMapping("/music")
    public ResponseEntity<MusicResponse> insert(@RequestBody @Validated MusicRequest musicRequest, UriComponentsBuilder uriBuilder) {
        Music music = musicService.insert(musicRequest.getTitle(), musicRequest.getArtist());
        URI location = uriBuilder.path("/music/{id}").buildAndExpand(music.getId()).toUri();
        MusicResponse body = new MusicResponse("music created");
        return ResponseEntity.created(location).body(body);
    }

    /**
     * 更新処理
     * ステータスコード200で更新後のデータがJSON型で出力される実装。
     */
//    @PatchMapping("/music/{id}")
//    public Music update(@PathVariable("id") Integer id, @RequestBody MusicRequest musicRequest) {
//        Music music = musicService.update(id, musicRequest.getTitle(), musicRequest.getArtist());
//        return music;
//    }

    /**
     * 更新処理
     * 更新が成功した場合はメッセージ（music updated）を返す。
     *
     * @param id
     * @param musicRequest
     * @return
     */
    @PatchMapping("/music/{id}")
    public ResponseEntity<MusicResponse> update(@PathVariable("id") Integer id, @RequestBody MusicRequest musicRequest) {
        Music music = musicService.update(id, musicRequest.getTitle(), musicRequest.getArtist());
        MusicResponse body = new MusicResponse("music updated");
        return ResponseEntity.ok(body);
    }

    /**
     * 削除処理（）
     */
    @DeleteMapping("/music/{id}")
    public ResponseEntity<MusicResponse> delete(@PathVariable("id") Integer id) {
        Music music = musicService.delete(id);
        MusicResponse body = new MusicResponse("music delete");
        return ResponseEntity.ok(body);
    }
}
